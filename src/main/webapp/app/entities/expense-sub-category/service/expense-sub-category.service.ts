import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IExpenseSubCategory, NewExpenseSubCategory } from '../expense-sub-category.model';

export type PartialUpdateExpenseSubCategory = Partial<IExpenseSubCategory> & Pick<IExpenseSubCategory, 'id'>;

type RestOf<T extends IExpenseSubCategory | NewExpenseSubCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestExpenseSubCategory = RestOf<IExpenseSubCategory>;

export type NewRestExpenseSubCategory = RestOf<NewExpenseSubCategory>;

export type PartialUpdateRestExpenseSubCategory = RestOf<PartialUpdateExpenseSubCategory>;

export type EntityResponseType = HttpResponse<IExpenseSubCategory>;
export type EntityArrayResponseType = HttpResponse<IExpenseSubCategory[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseSubCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-sub-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/expense-sub-categories/_search');

  create(expenseSubCategory: NewExpenseSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseSubCategory);
    return this.http
      .post<RestExpenseSubCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(expenseSubCategory: IExpenseSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseSubCategory);
    return this.http
      .put<RestExpenseSubCategory>(`${this.resourceUrl}/${this.getExpenseSubCategoryIdentifier(expenseSubCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(expenseSubCategory: PartialUpdateExpenseSubCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseSubCategory);
    return this.http
      .patch<RestExpenseSubCategory>(`${this.resourceUrl}/${this.getExpenseSubCategoryIdentifier(expenseSubCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExpenseSubCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExpenseSubCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExpenseSubCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExpenseSubCategory[]>()], asapScheduler)),
    );
  }

  getExpenseSubCategoryIdentifier(expenseSubCategory: Pick<IExpenseSubCategory, 'id'>): number {
    return expenseSubCategory.id;
  }

  compareExpenseSubCategory(o1: Pick<IExpenseSubCategory, 'id'> | null, o2: Pick<IExpenseSubCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseSubCategoryIdentifier(o1) === this.getExpenseSubCategoryIdentifier(o2) : o1 === o2;
  }

  addExpenseSubCategoryToCollectionIfMissing<Type extends Pick<IExpenseSubCategory, 'id'>>(
    expenseSubCategoryCollection: Type[],
    ...expenseSubCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseSubCategories: Type[] = expenseSubCategoriesToCheck.filter(isPresent);
    if (expenseSubCategories.length > 0) {
      const expenseSubCategoryCollectionIdentifiers = expenseSubCategoryCollection.map(expenseSubCategoryItem =>
        this.getExpenseSubCategoryIdentifier(expenseSubCategoryItem),
      );
      const expenseSubCategoriesToAdd = expenseSubCategories.filter(expenseSubCategoryItem => {
        const expenseSubCategoryIdentifier = this.getExpenseSubCategoryIdentifier(expenseSubCategoryItem);
        if (expenseSubCategoryCollectionIdentifiers.includes(expenseSubCategoryIdentifier)) {
          return false;
        }
        expenseSubCategoryCollectionIdentifiers.push(expenseSubCategoryIdentifier);
        return true;
      });
      return [...expenseSubCategoriesToAdd, ...expenseSubCategoryCollection];
    }
    return expenseSubCategoryCollection;
  }

  protected convertDateFromClient<T extends IExpenseSubCategory | NewExpenseSubCategory | PartialUpdateExpenseSubCategory>(
    expenseSubCategory: T,
  ): RestOf<T> {
    return {
      ...expenseSubCategory,
      createdDate: expenseSubCategory.createdDate?.toJSON() ?? null,
      lastModifiedDate: expenseSubCategory.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExpenseSubCategory: RestExpenseSubCategory): IExpenseSubCategory {
    return {
      ...restExpenseSubCategory,
      createdDate: restExpenseSubCategory.createdDate ? dayjs(restExpenseSubCategory.createdDate) : undefined,
      lastModifiedDate: restExpenseSubCategory.lastModifiedDate ? dayjs(restExpenseSubCategory.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExpenseSubCategory>): HttpResponse<IExpenseSubCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExpenseSubCategory[]>): HttpResponse<IExpenseSubCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
