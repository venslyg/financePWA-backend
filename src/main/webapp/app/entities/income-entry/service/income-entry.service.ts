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
import { IIncomeEntry, NewIncomeEntry } from '../income-entry.model';

export type PartialUpdateIncomeEntry = Partial<IIncomeEntry> & Pick<IIncomeEntry, 'id'>;

type RestOf<T extends IIncomeEntry | NewIncomeEntry> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestIncomeEntry = RestOf<IIncomeEntry>;

export type NewRestIncomeEntry = RestOf<NewIncomeEntry>;

export type PartialUpdateRestIncomeEntry = RestOf<PartialUpdateIncomeEntry>;

export type EntityResponseType = HttpResponse<IIncomeEntry>;
export type EntityArrayResponseType = HttpResponse<IIncomeEntry[]>;

@Injectable({ providedIn: 'root' })
export class IncomeEntryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/income-entries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/income-entries/_search');

  create(incomeEntry: NewIncomeEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeEntry);
    return this.http
      .post<RestIncomeEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incomeEntry: IIncomeEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeEntry);
    return this.http
      .put<RestIncomeEntry>(`${this.resourceUrl}/${this.getIncomeEntryIdentifier(incomeEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incomeEntry: PartialUpdateIncomeEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incomeEntry);
    return this.http
      .patch<RestIncomeEntry>(`${this.resourceUrl}/${this.getIncomeEntryIdentifier(incomeEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIncomeEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncomeEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestIncomeEntry[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IIncomeEntry[]>()], asapScheduler)),
    );
  }

  getIncomeEntryIdentifier(incomeEntry: Pick<IIncomeEntry, 'id'>): number {
    return incomeEntry.id;
  }

  compareIncomeEntry(o1: Pick<IIncomeEntry, 'id'> | null, o2: Pick<IIncomeEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncomeEntryIdentifier(o1) === this.getIncomeEntryIdentifier(o2) : o1 === o2;
  }

  addIncomeEntryToCollectionIfMissing<Type extends Pick<IIncomeEntry, 'id'>>(
    incomeEntryCollection: Type[],
    ...incomeEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incomeEntries: Type[] = incomeEntriesToCheck.filter(isPresent);
    if (incomeEntries.length > 0) {
      const incomeEntryCollectionIdentifiers = incomeEntryCollection.map(incomeEntryItem => this.getIncomeEntryIdentifier(incomeEntryItem));
      const incomeEntriesToAdd = incomeEntries.filter(incomeEntryItem => {
        const incomeEntryIdentifier = this.getIncomeEntryIdentifier(incomeEntryItem);
        if (incomeEntryCollectionIdentifiers.includes(incomeEntryIdentifier)) {
          return false;
        }
        incomeEntryCollectionIdentifiers.push(incomeEntryIdentifier);
        return true;
      });
      return [...incomeEntriesToAdd, ...incomeEntryCollection];
    }
    return incomeEntryCollection;
  }

  protected convertDateFromClient<T extends IIncomeEntry | NewIncomeEntry | PartialUpdateIncomeEntry>(incomeEntry: T): RestOf<T> {
    return {
      ...incomeEntry,
      date: incomeEntry.date?.format(DATE_FORMAT) ?? null,
      createdDate: incomeEntry.createdDate?.toJSON() ?? null,
      lastModifiedDate: incomeEntry.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIncomeEntry: RestIncomeEntry): IIncomeEntry {
    return {
      ...restIncomeEntry,
      date: restIncomeEntry.date ? dayjs(restIncomeEntry.date) : undefined,
      createdDate: restIncomeEntry.createdDate ? dayjs(restIncomeEntry.createdDate) : undefined,
      lastModifiedDate: restIncomeEntry.lastModifiedDate ? dayjs(restIncomeEntry.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIncomeEntry>): HttpResponse<IIncomeEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIncomeEntry[]>): HttpResponse<IIncomeEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
