import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountSet, NewAccountSet } from '../account-set.model';

export type PartialUpdateAccountSet = Partial<IAccountSet> & Pick<IAccountSet, 'id'>;

type RestOf<T extends IAccountSet | NewAccountSet> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestAccountSet = RestOf<IAccountSet>;

export type NewRestAccountSet = RestOf<NewAccountSet>;

export type PartialUpdateRestAccountSet = RestOf<PartialUpdateAccountSet>;

export type EntityResponseType = HttpResponse<IAccountSet>;
export type EntityArrayResponseType = HttpResponse<IAccountSet[]>;

@Injectable({ providedIn: 'root' })
export class AccountSetService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-sets');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/account-sets/_search');

  create(accountSet: NewAccountSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountSet);
    return this.http
      .post<RestAccountSet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(accountSet: IAccountSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountSet);
    return this.http
      .put<RestAccountSet>(`${this.resourceUrl}/${this.getAccountSetIdentifier(accountSet)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(accountSet: PartialUpdateAccountSet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountSet);
    return this.http
      .patch<RestAccountSet>(`${this.resourceUrl}/${this.getAccountSetIdentifier(accountSet)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAccountSet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAccountSet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAccountSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAccountSet[]>()], asapScheduler)),
    );
  }

  getAccountSetIdentifier(accountSet: Pick<IAccountSet, 'id'>): number {
    return accountSet.id;
  }

  compareAccountSet(o1: Pick<IAccountSet, 'id'> | null, o2: Pick<IAccountSet, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountSetIdentifier(o1) === this.getAccountSetIdentifier(o2) : o1 === o2;
  }

  addAccountSetToCollectionIfMissing<Type extends Pick<IAccountSet, 'id'>>(
    accountSetCollection: Type[],
    ...accountSetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountSets: Type[] = accountSetsToCheck.filter(isPresent);
    if (accountSets.length > 0) {
      const accountSetCollectionIdentifiers = accountSetCollection.map(accountSetItem => this.getAccountSetIdentifier(accountSetItem));
      const accountSetsToAdd = accountSets.filter(accountSetItem => {
        const accountSetIdentifier = this.getAccountSetIdentifier(accountSetItem);
        if (accountSetCollectionIdentifiers.includes(accountSetIdentifier)) {
          return false;
        }
        accountSetCollectionIdentifiers.push(accountSetIdentifier);
        return true;
      });
      return [...accountSetsToAdd, ...accountSetCollection];
    }
    return accountSetCollection;
  }

  protected convertDateFromClient<T extends IAccountSet | NewAccountSet | PartialUpdateAccountSet>(accountSet: T): RestOf<T> {
    return {
      ...accountSet,
      createdDate: accountSet.createdDate?.toJSON() ?? null,
      lastModifiedDate: accountSet.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAccountSet: RestAccountSet): IAccountSet {
    return {
      ...restAccountSet,
      createdDate: restAccountSet.createdDate ? dayjs(restAccountSet.createdDate) : undefined,
      lastModifiedDate: restAccountSet.lastModifiedDate ? dayjs(restAccountSet.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAccountSet>): HttpResponse<IAccountSet> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAccountSet[]>): HttpResponse<IAccountSet[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
