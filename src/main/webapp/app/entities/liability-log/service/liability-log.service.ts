import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILiabilityLog, NewLiabilityLog } from '../liability-log.model';

export type PartialUpdateLiabilityLog = Partial<ILiabilityLog> & Pick<ILiabilityLog, 'id'>;

type RestOf<T extends ILiabilityLog | NewLiabilityLog> = Omit<T, 'startDate' | 'endDate' | 'createdDate' | 'lastModifiedDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestLiabilityLog = RestOf<ILiabilityLog>;

export type NewRestLiabilityLog = RestOf<NewLiabilityLog>;

export type PartialUpdateRestLiabilityLog = RestOf<PartialUpdateLiabilityLog>;

export type EntityResponseType = HttpResponse<ILiabilityLog>;
export type EntityArrayResponseType = HttpResponse<ILiabilityLog[]>;

@Injectable({ providedIn: 'root' })
export class LiabilityLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/liability-logs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/liability-logs/_search');

  create(liabilityLog: NewLiabilityLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(liabilityLog);
    return this.http
      .post<RestLiabilityLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(liabilityLog: ILiabilityLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(liabilityLog);
    return this.http
      .put<RestLiabilityLog>(`${this.resourceUrl}/${this.getLiabilityLogIdentifier(liabilityLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(liabilityLog: PartialUpdateLiabilityLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(liabilityLog);
    return this.http
      .patch<RestLiabilityLog>(`${this.resourceUrl}/${this.getLiabilityLogIdentifier(liabilityLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLiabilityLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLiabilityLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestLiabilityLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ILiabilityLog[]>()], asapScheduler)),
    );
  }

  getLiabilityLogIdentifier(liabilityLog: Pick<ILiabilityLog, 'id'>): number {
    return liabilityLog.id;
  }

  compareLiabilityLog(o1: Pick<ILiabilityLog, 'id'> | null, o2: Pick<ILiabilityLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getLiabilityLogIdentifier(o1) === this.getLiabilityLogIdentifier(o2) : o1 === o2;
  }

  addLiabilityLogToCollectionIfMissing<Type extends Pick<ILiabilityLog, 'id'>>(
    liabilityLogCollection: Type[],
    ...liabilityLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const liabilityLogs: Type[] = liabilityLogsToCheck.filter(isPresent);
    if (liabilityLogs.length > 0) {
      const liabilityLogCollectionIdentifiers = liabilityLogCollection.map(liabilityLogItem =>
        this.getLiabilityLogIdentifier(liabilityLogItem),
      );
      const liabilityLogsToAdd = liabilityLogs.filter(liabilityLogItem => {
        const liabilityLogIdentifier = this.getLiabilityLogIdentifier(liabilityLogItem);
        if (liabilityLogCollectionIdentifiers.includes(liabilityLogIdentifier)) {
          return false;
        }
        liabilityLogCollectionIdentifiers.push(liabilityLogIdentifier);
        return true;
      });
      return [...liabilityLogsToAdd, ...liabilityLogCollection];
    }
    return liabilityLogCollection;
  }

  protected convertDateFromClient<T extends ILiabilityLog | NewLiabilityLog | PartialUpdateLiabilityLog>(liabilityLog: T): RestOf<T> {
    return {
      ...liabilityLog,
      startDate: liabilityLog.startDate?.format(DATE_FORMAT) ?? null,
      endDate: liabilityLog.endDate?.format(DATE_FORMAT) ?? null,
      createdDate: liabilityLog.createdDate?.toJSON() ?? null,
      lastModifiedDate: liabilityLog.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLiabilityLog: RestLiabilityLog): ILiabilityLog {
    return {
      ...restLiabilityLog,
      startDate: restLiabilityLog.startDate ? dayjs(restLiabilityLog.startDate) : undefined,
      endDate: restLiabilityLog.endDate ? dayjs(restLiabilityLog.endDate) : undefined,
      createdDate: restLiabilityLog.createdDate ? dayjs(restLiabilityLog.createdDate) : undefined,
      lastModifiedDate: restLiabilityLog.lastModifiedDate ? dayjs(restLiabilityLog.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLiabilityLog>): HttpResponse<ILiabilityLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLiabilityLog[]>): HttpResponse<ILiabilityLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
