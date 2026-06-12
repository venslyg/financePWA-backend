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
import { IAssetDepreciationHistory, NewAssetDepreciationHistory } from '../asset-depreciation-history.model';

export type PartialUpdateAssetDepreciationHistory = Partial<IAssetDepreciationHistory> & Pick<IAssetDepreciationHistory, 'id'>;

type RestOf<T extends IAssetDepreciationHistory | NewAssetDepreciationHistory> = Omit<
  T,
  'depreciationDate' | 'createdDate' | 'lastModifiedDate'
> & {
  depreciationDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestAssetDepreciationHistory = RestOf<IAssetDepreciationHistory>;

export type NewRestAssetDepreciationHistory = RestOf<NewAssetDepreciationHistory>;

export type PartialUpdateRestAssetDepreciationHistory = RestOf<PartialUpdateAssetDepreciationHistory>;

export type EntityResponseType = HttpResponse<IAssetDepreciationHistory>;
export type EntityArrayResponseType = HttpResponse<IAssetDepreciationHistory[]>;

@Injectable({ providedIn: 'root' })
export class AssetDepreciationHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-depreciation-histories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/asset-depreciation-histories/_search');

  create(assetDepreciationHistory: NewAssetDepreciationHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDepreciationHistory);
    return this.http
      .post<RestAssetDepreciationHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(assetDepreciationHistory: IAssetDepreciationHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDepreciationHistory);
    return this.http
      .put<RestAssetDepreciationHistory>(
        `${this.resourceUrl}/${this.getAssetDepreciationHistoryIdentifier(assetDepreciationHistory)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(assetDepreciationHistory: PartialUpdateAssetDepreciationHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDepreciationHistory);
    return this.http
      .patch<RestAssetDepreciationHistory>(
        `${this.resourceUrl}/${this.getAssetDepreciationHistoryIdentifier(assetDepreciationHistory)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAssetDepreciationHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAssetDepreciationHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAssetDepreciationHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAssetDepreciationHistory[]>()], asapScheduler)),
    );
  }

  getAssetDepreciationHistoryIdentifier(assetDepreciationHistory: Pick<IAssetDepreciationHistory, 'id'>): number {
    return assetDepreciationHistory.id;
  }

  compareAssetDepreciationHistory(
    o1: Pick<IAssetDepreciationHistory, 'id'> | null,
    o2: Pick<IAssetDepreciationHistory, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getAssetDepreciationHistoryIdentifier(o1) === this.getAssetDepreciationHistoryIdentifier(o2) : o1 === o2;
  }

  addAssetDepreciationHistoryToCollectionIfMissing<Type extends Pick<IAssetDepreciationHistory, 'id'>>(
    assetDepreciationHistoryCollection: Type[],
    ...assetDepreciationHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetDepreciationHistories: Type[] = assetDepreciationHistoriesToCheck.filter(isPresent);
    if (assetDepreciationHistories.length > 0) {
      const assetDepreciationHistoryCollectionIdentifiers = assetDepreciationHistoryCollection.map(assetDepreciationHistoryItem =>
        this.getAssetDepreciationHistoryIdentifier(assetDepreciationHistoryItem),
      );
      const assetDepreciationHistoriesToAdd = assetDepreciationHistories.filter(assetDepreciationHistoryItem => {
        const assetDepreciationHistoryIdentifier = this.getAssetDepreciationHistoryIdentifier(assetDepreciationHistoryItem);
        if (assetDepreciationHistoryCollectionIdentifiers.includes(assetDepreciationHistoryIdentifier)) {
          return false;
        }
        assetDepreciationHistoryCollectionIdentifiers.push(assetDepreciationHistoryIdentifier);
        return true;
      });
      return [...assetDepreciationHistoriesToAdd, ...assetDepreciationHistoryCollection];
    }
    return assetDepreciationHistoryCollection;
  }

  protected convertDateFromClient<
    T extends IAssetDepreciationHistory | NewAssetDepreciationHistory | PartialUpdateAssetDepreciationHistory,
  >(assetDepreciationHistory: T): RestOf<T> {
    return {
      ...assetDepreciationHistory,
      depreciationDate: assetDepreciationHistory.depreciationDate?.format(DATE_FORMAT) ?? null,
      createdDate: assetDepreciationHistory.createdDate?.toJSON() ?? null,
      lastModifiedDate: assetDepreciationHistory.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAssetDepreciationHistory: RestAssetDepreciationHistory): IAssetDepreciationHistory {
    return {
      ...restAssetDepreciationHistory,
      depreciationDate: restAssetDepreciationHistory.depreciationDate ? dayjs(restAssetDepreciationHistory.depreciationDate) : undefined,
      createdDate: restAssetDepreciationHistory.createdDate ? dayjs(restAssetDepreciationHistory.createdDate) : undefined,
      lastModifiedDate: restAssetDepreciationHistory.lastModifiedDate ? dayjs(restAssetDepreciationHistory.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAssetDepreciationHistory>): HttpResponse<IAssetDepreciationHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAssetDepreciationHistory[]>): HttpResponse<IAssetDepreciationHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
