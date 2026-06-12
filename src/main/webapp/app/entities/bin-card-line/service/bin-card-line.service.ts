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
import { IBinCardLine, NewBinCardLine } from '../bin-card-line.model';

export type PartialUpdateBinCardLine = Partial<IBinCardLine> & Pick<IBinCardLine, 'id'>;

type RestOf<T extends IBinCardLine | NewBinCardLine> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBinCardLine = RestOf<IBinCardLine>;

export type NewRestBinCardLine = RestOf<NewBinCardLine>;

export type PartialUpdateRestBinCardLine = RestOf<PartialUpdateBinCardLine>;

export type EntityResponseType = HttpResponse<IBinCardLine>;
export type EntityArrayResponseType = HttpResponse<IBinCardLine[]>;

@Injectable({ providedIn: 'root' })
export class BinCardLineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bin-card-lines');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/bin-card-lines/_search');

  create(binCardLine: NewBinCardLine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(binCardLine);
    return this.http
      .post<RestBinCardLine>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(binCardLine: IBinCardLine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(binCardLine);
    return this.http
      .put<RestBinCardLine>(`${this.resourceUrl}/${this.getBinCardLineIdentifier(binCardLine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(binCardLine: PartialUpdateBinCardLine): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(binCardLine);
    return this.http
      .patch<RestBinCardLine>(`${this.resourceUrl}/${this.getBinCardLineIdentifier(binCardLine)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBinCardLine>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBinCardLine[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBinCardLine[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IBinCardLine[]>()], asapScheduler)),
    );
  }

  getBinCardLineIdentifier(binCardLine: Pick<IBinCardLine, 'id'>): number {
    return binCardLine.id;
  }

  compareBinCardLine(o1: Pick<IBinCardLine, 'id'> | null, o2: Pick<IBinCardLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getBinCardLineIdentifier(o1) === this.getBinCardLineIdentifier(o2) : o1 === o2;
  }

  addBinCardLineToCollectionIfMissing<Type extends Pick<IBinCardLine, 'id'>>(
    binCardLineCollection: Type[],
    ...binCardLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const binCardLines: Type[] = binCardLinesToCheck.filter(isPresent);
    if (binCardLines.length > 0) {
      const binCardLineCollectionIdentifiers = binCardLineCollection.map(binCardLineItem => this.getBinCardLineIdentifier(binCardLineItem));
      const binCardLinesToAdd = binCardLines.filter(binCardLineItem => {
        const binCardLineIdentifier = this.getBinCardLineIdentifier(binCardLineItem);
        if (binCardLineCollectionIdentifiers.includes(binCardLineIdentifier)) {
          return false;
        }
        binCardLineCollectionIdentifiers.push(binCardLineIdentifier);
        return true;
      });
      return [...binCardLinesToAdd, ...binCardLineCollection];
    }
    return binCardLineCollection;
  }

  protected convertDateFromClient<T extends IBinCardLine | NewBinCardLine | PartialUpdateBinCardLine>(binCardLine: T): RestOf<T> {
    return {
      ...binCardLine,
      date: binCardLine.date?.format(DATE_FORMAT) ?? null,
      createdDate: binCardLine.createdDate?.toJSON() ?? null,
      lastModifiedDate: binCardLine.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBinCardLine: RestBinCardLine): IBinCardLine {
    return {
      ...restBinCardLine,
      date: restBinCardLine.date ? dayjs(restBinCardLine.date) : undefined,
      createdDate: restBinCardLine.createdDate ? dayjs(restBinCardLine.createdDate) : undefined,
      lastModifiedDate: restBinCardLine.lastModifiedDate ? dayjs(restBinCardLine.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBinCardLine>): HttpResponse<IBinCardLine> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBinCardLine[]>): HttpResponse<IBinCardLine[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
