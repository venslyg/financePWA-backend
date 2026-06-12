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
import { IPettyCashLedger, NewPettyCashLedger } from '../petty-cash-ledger.model';

export type PartialUpdatePettyCashLedger = Partial<IPettyCashLedger> & Pick<IPettyCashLedger, 'id'>;

type RestOf<T extends IPettyCashLedger | NewPettyCashLedger> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestPettyCashLedger = RestOf<IPettyCashLedger>;

export type NewRestPettyCashLedger = RestOf<NewPettyCashLedger>;

export type PartialUpdateRestPettyCashLedger = RestOf<PartialUpdatePettyCashLedger>;

export type EntityResponseType = HttpResponse<IPettyCashLedger>;
export type EntityArrayResponseType = HttpResponse<IPettyCashLedger[]>;

@Injectable({ providedIn: 'root' })
export class PettyCashLedgerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/petty-cash-ledgers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/petty-cash-ledgers/_search');

  create(pettyCashLedger: NewPettyCashLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pettyCashLedger);
    return this.http
      .post<RestPettyCashLedger>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pettyCashLedger: IPettyCashLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pettyCashLedger);
    return this.http
      .put<RestPettyCashLedger>(`${this.resourceUrl}/${this.getPettyCashLedgerIdentifier(pettyCashLedger)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pettyCashLedger: PartialUpdatePettyCashLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pettyCashLedger);
    return this.http
      .patch<RestPettyCashLedger>(`${this.resourceUrl}/${this.getPettyCashLedgerIdentifier(pettyCashLedger)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPettyCashLedger>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPettyCashLedger[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestPettyCashLedger[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IPettyCashLedger[]>()], asapScheduler)),
    );
  }

  getPettyCashLedgerIdentifier(pettyCashLedger: Pick<IPettyCashLedger, 'id'>): number {
    return pettyCashLedger.id;
  }

  comparePettyCashLedger(o1: Pick<IPettyCashLedger, 'id'> | null, o2: Pick<IPettyCashLedger, 'id'> | null): boolean {
    return o1 && o2 ? this.getPettyCashLedgerIdentifier(o1) === this.getPettyCashLedgerIdentifier(o2) : o1 === o2;
  }

  addPettyCashLedgerToCollectionIfMissing<Type extends Pick<IPettyCashLedger, 'id'>>(
    pettyCashLedgerCollection: Type[],
    ...pettyCashLedgersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pettyCashLedgers: Type[] = pettyCashLedgersToCheck.filter(isPresent);
    if (pettyCashLedgers.length > 0) {
      const pettyCashLedgerCollectionIdentifiers = pettyCashLedgerCollection.map(pettyCashLedgerItem =>
        this.getPettyCashLedgerIdentifier(pettyCashLedgerItem),
      );
      const pettyCashLedgersToAdd = pettyCashLedgers.filter(pettyCashLedgerItem => {
        const pettyCashLedgerIdentifier = this.getPettyCashLedgerIdentifier(pettyCashLedgerItem);
        if (pettyCashLedgerCollectionIdentifiers.includes(pettyCashLedgerIdentifier)) {
          return false;
        }
        pettyCashLedgerCollectionIdentifiers.push(pettyCashLedgerIdentifier);
        return true;
      });
      return [...pettyCashLedgersToAdd, ...pettyCashLedgerCollection];
    }
    return pettyCashLedgerCollection;
  }

  protected convertDateFromClient<T extends IPettyCashLedger | NewPettyCashLedger | PartialUpdatePettyCashLedger>(
    pettyCashLedger: T,
  ): RestOf<T> {
    return {
      ...pettyCashLedger,
      date: pettyCashLedger.date?.format(DATE_FORMAT) ?? null,
      createdDate: pettyCashLedger.createdDate?.toJSON() ?? null,
      lastModifiedDate: pettyCashLedger.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPettyCashLedger: RestPettyCashLedger): IPettyCashLedger {
    return {
      ...restPettyCashLedger,
      date: restPettyCashLedger.date ? dayjs(restPettyCashLedger.date) : undefined,
      createdDate: restPettyCashLedger.createdDate ? dayjs(restPettyCashLedger.createdDate) : undefined,
      lastModifiedDate: restPettyCashLedger.lastModifiedDate ? dayjs(restPettyCashLedger.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPettyCashLedger>): HttpResponse<IPettyCashLedger> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPettyCashLedger[]>): HttpResponse<IPettyCashLedger[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
