import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAssetCategory, NewAssetCategory } from '../asset-category.model';

export type PartialUpdateAssetCategory = Partial<IAssetCategory> & Pick<IAssetCategory, 'id'>;

type RestOf<T extends IAssetCategory | NewAssetCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestAssetCategory = RestOf<IAssetCategory>;

export type NewRestAssetCategory = RestOf<NewAssetCategory>;

export type PartialUpdateRestAssetCategory = RestOf<PartialUpdateAssetCategory>;

export type EntityResponseType = HttpResponse<IAssetCategory>;
export type EntityArrayResponseType = HttpResponse<IAssetCategory[]>;

@Injectable({ providedIn: 'root' })
export class AssetCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/asset-categories/_search');

  create(assetCategory: NewAssetCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetCategory);
    return this.http
      .post<RestAssetCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(assetCategory: IAssetCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetCategory);
    return this.http
      .put<RestAssetCategory>(`${this.resourceUrl}/${this.getAssetCategoryIdentifier(assetCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(assetCategory: PartialUpdateAssetCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetCategory);
    return this.http
      .patch<RestAssetCategory>(`${this.resourceUrl}/${this.getAssetCategoryIdentifier(assetCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAssetCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAssetCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAssetCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAssetCategory[]>()], asapScheduler)),
    );
  }

  getAssetCategoryIdentifier(assetCategory: Pick<IAssetCategory, 'id'>): number {
    return assetCategory.id;
  }

  compareAssetCategory(o1: Pick<IAssetCategory, 'id'> | null, o2: Pick<IAssetCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetCategoryIdentifier(o1) === this.getAssetCategoryIdentifier(o2) : o1 === o2;
  }

  addAssetCategoryToCollectionIfMissing<Type extends Pick<IAssetCategory, 'id'>>(
    assetCategoryCollection: Type[],
    ...assetCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetCategories: Type[] = assetCategoriesToCheck.filter(isPresent);
    if (assetCategories.length > 0) {
      const assetCategoryCollectionIdentifiers = assetCategoryCollection.map(assetCategoryItem =>
        this.getAssetCategoryIdentifier(assetCategoryItem),
      );
      const assetCategoriesToAdd = assetCategories.filter(assetCategoryItem => {
        const assetCategoryIdentifier = this.getAssetCategoryIdentifier(assetCategoryItem);
        if (assetCategoryCollectionIdentifiers.includes(assetCategoryIdentifier)) {
          return false;
        }
        assetCategoryCollectionIdentifiers.push(assetCategoryIdentifier);
        return true;
      });
      return [...assetCategoriesToAdd, ...assetCategoryCollection];
    }
    return assetCategoryCollection;
  }

  protected convertDateFromClient<T extends IAssetCategory | NewAssetCategory | PartialUpdateAssetCategory>(assetCategory: T): RestOf<T> {
    return {
      ...assetCategory,
      createdDate: assetCategory.createdDate?.toJSON() ?? null,
      lastModifiedDate: assetCategory.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAssetCategory: RestAssetCategory): IAssetCategory {
    return {
      ...restAssetCategory,
      createdDate: restAssetCategory.createdDate ? dayjs(restAssetCategory.createdDate) : undefined,
      lastModifiedDate: restAssetCategory.lastModifiedDate ? dayjs(restAssetCategory.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAssetCategory>): HttpResponse<IAssetCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAssetCategory[]>): HttpResponse<IAssetCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
