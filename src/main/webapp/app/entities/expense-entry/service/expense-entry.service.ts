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
import { IExpenseEntry, NewExpenseEntry } from '../expense-entry.model';

export type PartialUpdateExpenseEntry = Partial<IExpenseEntry> & Pick<IExpenseEntry, 'id'>;

type RestOf<T extends IExpenseEntry | NewExpenseEntry> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestExpenseEntry = RestOf<IExpenseEntry>;

export type NewRestExpenseEntry = RestOf<NewExpenseEntry>;

export type PartialUpdateRestExpenseEntry = RestOf<PartialUpdateExpenseEntry>;

export type EntityResponseType = HttpResponse<IExpenseEntry>;
export type EntityArrayResponseType = HttpResponse<IExpenseEntry[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseEntryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-entries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/expense-entries/_search');

  create(expenseEntry: NewExpenseEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseEntry);
    return this.http
      .post<RestExpenseEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(expenseEntry: IExpenseEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseEntry);
    return this.http
      .put<RestExpenseEntry>(`${this.resourceUrl}/${this.getExpenseEntryIdentifier(expenseEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(expenseEntry: PartialUpdateExpenseEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseEntry);
    return this.http
      .patch<RestExpenseEntry>(`${this.resourceUrl}/${this.getExpenseEntryIdentifier(expenseEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExpenseEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExpenseEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExpenseEntry[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExpenseEntry[]>()], asapScheduler)),
    );
  }

  getExpenseEntryIdentifier(expenseEntry: Pick<IExpenseEntry, 'id'>): number {
    return expenseEntry.id;
  }

  compareExpenseEntry(o1: Pick<IExpenseEntry, 'id'> | null, o2: Pick<IExpenseEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseEntryIdentifier(o1) === this.getExpenseEntryIdentifier(o2) : o1 === o2;
  }

  addExpenseEntryToCollectionIfMissing<Type extends Pick<IExpenseEntry, 'id'>>(
    expenseEntryCollection: Type[],
    ...expenseEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseEntries: Type[] = expenseEntriesToCheck.filter(isPresent);
    if (expenseEntries.length > 0) {
      const expenseEntryCollectionIdentifiers = expenseEntryCollection.map(expenseEntryItem =>
        this.getExpenseEntryIdentifier(expenseEntryItem),
      );
      const expenseEntriesToAdd = expenseEntries.filter(expenseEntryItem => {
        const expenseEntryIdentifier = this.getExpenseEntryIdentifier(expenseEntryItem);
        if (expenseEntryCollectionIdentifiers.includes(expenseEntryIdentifier)) {
          return false;
        }
        expenseEntryCollectionIdentifiers.push(expenseEntryIdentifier);
        return true;
      });
      return [...expenseEntriesToAdd, ...expenseEntryCollection];
    }
    return expenseEntryCollection;
  }

  protected convertDateFromClient<T extends IExpenseEntry | NewExpenseEntry | PartialUpdateExpenseEntry>(expenseEntry: T): RestOf<T> {
    return {
      ...expenseEntry,
      date: expenseEntry.date?.format(DATE_FORMAT) ?? null,
      createdDate: expenseEntry.createdDate?.toJSON() ?? null,
      lastModifiedDate: expenseEntry.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExpenseEntry: RestExpenseEntry): IExpenseEntry {
    return {
      ...restExpenseEntry,
      date: restExpenseEntry.date ? dayjs(restExpenseEntry.date) : undefined,
      createdDate: restExpenseEntry.createdDate ? dayjs(restExpenseEntry.createdDate) : undefined,
      lastModifiedDate: restExpenseEntry.lastModifiedDate ? dayjs(restExpenseEntry.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExpenseEntry>): HttpResponse<IExpenseEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExpenseEntry[]>): HttpResponse<IExpenseEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
