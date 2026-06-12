import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAssetSubCategory, NewAssetSubCategory } from '../asset-sub-category.model';

export type PartialUpdateAssetSubCategory = Partial<IAssetSubCategory> & Pick<IAssetSubCategory, 'id'>;

type RestOf<T extends IAssetSubCategory | NewAssetSubCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestAssetSubCategory = RestOf<IAssetSubCategory>;

export type NewRestAssetSubCategory = RestOf<NewAssetSubCategory>;

export type PartialUpdateRestAssetSubCategory = RestOf<PartialUpdateAssetSubCategory>;

export type EntityResponseType = HttpResponse<IAssetSubCategory>;
export type EntityArrayResponseType = HttpResponse<IAssetSubCategory[]>;

@Injectable({ providedIn: 'root' })
export class AssetSubCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-sub-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/asset-sub-categories/_search');

  create(assetSubCategory: NewAssetSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetSubCategory);
    return this.http
      .post<RestAssetSubCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(assetSubCategory: IAssetSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetSubCategory);
    return this.http
      .put<RestAssetSubCategory>(`${this.resourceUrl}/${this.getAssetSubCategoryIdentifier(assetSubCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(assetSubCategory: PartialUpdateAssetSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetSubCategory);
    return this.http
      .patch<RestAssetSubCategory>(`${this.resourceUrl}/${this.getAssetSubCategoryIdentifier(assetSubCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAssetSubCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAssetSubCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAssetSubCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAssetSubCategory[]>()], asapScheduler)),
    );
  }

  getAssetSubCategoryIdentifier(assetSubCategory: Pick<IAssetSubCategory, 'id'>): number {
    return assetSubCategory.id;
  }

  compareAssetSubCategory(o1: Pick<IAssetSubCategory, 'id'> | null, o2: Pick<IAssetSubCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetSubCategoryIdentifier(o1) === this.getAssetSubCategoryIdentifier(o2) : o1 === o2;
  }

  addAssetSubCategoryToCollectionIfMissing<Type extends Pick<IAssetSubCategory, 'id'>>(
    assetSubCategoryCollection: Type[],
    ...assetSubCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetSubCategories: Type[] = assetSubCategoriesToCheck.filter(isPresent);
    if (assetSubCategories.length > 0) {
      const assetSubCategoryCollectionIdentifiers = assetSubCategoryCollection.map(assetSubCategoryItem =>
        this.getAssetSubCategoryIdentifier(assetSubCategoryItem),
      );
      const assetSubCategoriesToAdd = assetSubCategories.filter(assetSubCategoryItem => {
        const assetSubCategoryIdentifier = this.getAssetSubCategoryIdentifier(assetSubCategoryItem);
        if (assetSubCategoryCollectionIdentifiers.includes(assetSubCategoryIdentifier)) {
          return false;
        }
        assetSubCategoryCollectionIdentifiers.push(assetSubCategoryIdentifier);
        return true;
      });
      return [...assetSubCategoriesToAdd, ...assetSubCategoryCollection];
    }
    return assetSubCategoryCollection;
  }

  protected convertDateFromClient<T extends IAssetSubCategory | NewAssetSubCategory | PartialUpdateAssetSubCategory>(
    assetSubCategory: T,
  ): RestOf<T> {
    return {
      ...assetSubCategory,
      createdDate: assetSubCategory.createdDate?.toJSON() ?? null,
      lastModifiedDate: assetSubCategory.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAssetSubCategory: RestAssetSubCategory): IAssetSubCategory {
    return {
      ...restAssetSubCategory,
      createdDate: restAssetSubCategory.createdDate ? dayjs(restAssetSubCategory.createdDate) : undefined,
      lastModifiedDate: restAssetSubCategory.lastModifiedDate ? dayjs(restAssetSubCategory.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAssetSubCategory>): HttpResponse<IAssetSubCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAssetSubCategory[]>): HttpResponse<IAssetSubCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
