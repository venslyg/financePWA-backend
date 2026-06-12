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
import { IBankLedger, NewBankLedger } from '../bank-ledger.model';

export type PartialUpdateBankLedger = Partial<IBankLedger> & Pick<IBankLedger, 'id'>;

type RestOf<T extends IBankLedger | NewBankLedger> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBankLedger = RestOf<IBankLedger>;

export type NewRestBankLedger = RestOf<NewBankLedger>;

export type PartialUpdateRestBankLedger = RestOf<PartialUpdateBankLedger>;

export type EntityResponseType = HttpResponse<IBankLedger>;
export type EntityArrayResponseType = HttpResponse<IBankLedger[]>;

@Injectable({ providedIn: 'root' })
export class BankLedgerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-ledgers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/bank-ledgers/_search');

  create(bankLedger: NewBankLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankLedger);
    return this.http
      .post<RestBankLedger>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bankLedger: IBankLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankLedger);
    return this.http
      .put<RestBankLedger>(`${this.resourceUrl}/${this.getBankLedgerIdentifier(bankLedger)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bankLedger: PartialUpdateBankLedger): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankLedger);
    return this.http
      .patch<RestBankLedger>(`${this.resourceUrl}/${this.getBankLedgerIdentifier(bankLedger)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBankLedger>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBankLedger[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBankLedger[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IBankLedger[]>()], asapScheduler)),
    );
  }

  getBankLedgerIdentifier(bankLedger: Pick<IBankLedger, 'id'>): number {
    return bankLedger.id;
  }

  compareBankLedger(o1: Pick<IBankLedger, 'id'> | null, o2: Pick<IBankLedger, 'id'> | null): boolean {
    return o1 && o2 ? this.getBankLedgerIdentifier(o1) === this.getBankLedgerIdentifier(o2) : o1 === o2;
  }

  addBankLedgerToCollectionIfMissing<Type extends Pick<IBankLedger, 'id'>>(
    bankLedgerCollection: Type[],
    ...bankLedgersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bankLedgers: Type[] = bankLedgersToCheck.filter(isPresent);
    if (bankLedgers.length > 0) {
      const bankLedgerCollectionIdentifiers = bankLedgerCollection.map(bankLedgerItem => this.getBankLedgerIdentifier(bankLedgerItem));
      const bankLedgersToAdd = bankLedgers.filter(bankLedgerItem => {
        const bankLedgerIdentifier = this.getBankLedgerIdentifier(bankLedgerItem);
        if (bankLedgerCollectionIdentifiers.includes(bankLedgerIdentifier)) {
          return false;
        }
        bankLedgerCollectionIdentifiers.push(bankLedgerIdentifier);
        return true;
      });
      return [...bankLedgersToAdd, ...bankLedgerCollection];
    }
    return bankLedgerCollection;
  }

  protected convertDateFromClient<T extends IBankLedger | NewBankLedger | PartialUpdateBankLedger>(bankLedger: T): RestOf<T> {
    return {
      ...bankLedger,
      date: bankLedger.date?.format(DATE_FORMAT) ?? null,
      createdDate: bankLedger.createdDate?.toJSON() ?? null,
      lastModifiedDate: bankLedger.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBankLedger: RestBankLedger): IBankLedger {
    return {
      ...restBankLedger,
      date: restBankLedger.date ? dayjs(restBankLedger.date) : undefined,
      createdDate: restBankLedger.createdDate ? dayjs(restBankLedger.createdDate) : undefined,
      lastModifiedDate: restBankLedger.lastModifiedDate ? dayjs(restBankLedger.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBankLedger>): HttpResponse<IBankLedger> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBankLedger[]>): HttpResponse<IBankLedger[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
