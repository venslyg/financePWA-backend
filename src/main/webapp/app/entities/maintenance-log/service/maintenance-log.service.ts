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
import { IMaintenanceLog, NewMaintenanceLog } from '../maintenance-log.model';

export type PartialUpdateMaintenanceLog = Partial<IMaintenanceLog> & Pick<IMaintenanceLog, 'id'>;

type RestOf<T extends IMaintenanceLog | NewMaintenanceLog> = Omit<T, 'logDate' | 'nextServiceDate' | 'createdDate' | 'lastModifiedDate'> & {
  logDate?: string | null;
  nextServiceDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestMaintenanceLog = RestOf<IMaintenanceLog>;

export type NewRestMaintenanceLog = RestOf<NewMaintenanceLog>;

export type PartialUpdateRestMaintenanceLog = RestOf<PartialUpdateMaintenanceLog>;

export type EntityResponseType = HttpResponse<IMaintenanceLog>;
export type EntityArrayResponseType = HttpResponse<IMaintenanceLog[]>;

@Injectable({ providedIn: 'root' })
export class MaintenanceLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maintenance-logs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/maintenance-logs/_search');

  create(maintenanceLog: NewMaintenanceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceLog);
    return this.http
      .post<RestMaintenanceLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(maintenanceLog: IMaintenanceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceLog);
    return this.http
      .put<RestMaintenanceLog>(`${this.resourceUrl}/${this.getMaintenanceLogIdentifier(maintenanceLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(maintenanceLog: PartialUpdateMaintenanceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintenanceLog);
    return this.http
      .patch<RestMaintenanceLog>(`${this.resourceUrl}/${this.getMaintenanceLogIdentifier(maintenanceLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaintenanceLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaintenanceLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMaintenanceLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMaintenanceLog[]>()], asapScheduler)),
    );
  }

  getMaintenanceLogIdentifier(maintenanceLog: Pick<IMaintenanceLog, 'id'>): number {
    return maintenanceLog.id;
  }

  compareMaintenanceLog(o1: Pick<IMaintenanceLog, 'id'> | null, o2: Pick<IMaintenanceLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaintenanceLogIdentifier(o1) === this.getMaintenanceLogIdentifier(o2) : o1 === o2;
  }

  addMaintenanceLogToCollectionIfMissing<Type extends Pick<IMaintenanceLog, 'id'>>(
    maintenanceLogCollection: Type[],
    ...maintenanceLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maintenanceLogs: Type[] = maintenanceLogsToCheck.filter(isPresent);
    if (maintenanceLogs.length > 0) {
      const maintenanceLogCollectionIdentifiers = maintenanceLogCollection.map(maintenanceLogItem =>
        this.getMaintenanceLogIdentifier(maintenanceLogItem),
      );
      const maintenanceLogsToAdd = maintenanceLogs.filter(maintenanceLogItem => {
        const maintenanceLogIdentifier = this.getMaintenanceLogIdentifier(maintenanceLogItem);
        if (maintenanceLogCollectionIdentifiers.includes(maintenanceLogIdentifier)) {
          return false;
        }
        maintenanceLogCollectionIdentifiers.push(maintenanceLogIdentifier);
        return true;
      });
      return [...maintenanceLogsToAdd, ...maintenanceLogCollection];
    }
    return maintenanceLogCollection;
  }

  protected convertDateFromClient<T extends IMaintenanceLog | NewMaintenanceLog | PartialUpdateMaintenanceLog>(
    maintenanceLog: T,
  ): RestOf<T> {
    return {
      ...maintenanceLog,
      logDate: maintenanceLog.logDate?.format(DATE_FORMAT) ?? null,
      nextServiceDate: maintenanceLog.nextServiceDate?.format(DATE_FORMAT) ?? null,
      createdDate: maintenanceLog.createdDate?.toJSON() ?? null,
      lastModifiedDate: maintenanceLog.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMaintenanceLog: RestMaintenanceLog): IMaintenanceLog {
    return {
      ...restMaintenanceLog,
      logDate: restMaintenanceLog.logDate ? dayjs(restMaintenanceLog.logDate) : undefined,
      nextServiceDate: restMaintenanceLog.nextServiceDate ? dayjs(restMaintenanceLog.nextServiceDate) : undefined,
      createdDate: restMaintenanceLog.createdDate ? dayjs(restMaintenanceLog.createdDate) : undefined,
      lastModifiedDate: restMaintenanceLog.lastModifiedDate ? dayjs(restMaintenanceLog.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaintenanceLog>): HttpResponse<IMaintenanceLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaintenanceLog[]>): HttpResponse<IMaintenanceLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
