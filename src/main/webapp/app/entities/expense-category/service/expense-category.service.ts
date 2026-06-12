import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IExpenseCategory, NewExpenseCategory } from '../expense-category.model';

export type PartialUpdateExpenseCategory = Partial<IExpenseCategory> & Pick<IExpenseCategory, 'id'>;

type RestOf<T extends IExpenseCategory | NewExpenseCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestExpenseCategory = RestOf<IExpenseCategory>;

export type NewRestExpenseCategory = RestOf<NewExpenseCategory>;

export type PartialUpdateRestExpenseCategory = RestOf<PartialUpdateExpenseCategory>;

export type EntityResponseType = HttpResponse<IExpenseCategory>;
export type EntityArrayResponseType = HttpResponse<IExpenseCategory[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/expense-categories/_search');

  create(expenseCategory: NewExpenseCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategory);
    return this.http
      .post<RestExpenseCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(expenseCategory: IExpenseCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategory);
    return this.http
      .put<RestExpenseCategory>(`${this.resourceUrl}/${this.getExpenseCategoryIdentifier(expenseCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(expenseCategory: PartialUpdateExpenseCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseCategory);
    return this.http
      .patch<RestExpenseCategory>(`${this.resourceUrl}/${this.getExpenseCategoryIdentifier(expenseCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExpenseCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExpenseCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExpenseCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExpenseCategory[]>()], asapScheduler)),
    );
  }

  getExpenseCategoryIdentifier(expenseCategory: Pick<IExpenseCategory, 'id'>): number {
    return expenseCategory.id;
  }

  compareExpenseCategory(o1: Pick<IExpenseCategory, 'id'> | null, o2: Pick<IExpenseCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseCategoryIdentifier(o1) === this.getExpenseCategoryIdentifier(o2) : o1 === o2;
  }

  addExpenseCategoryToCollectionIfMissing<Type extends Pick<IExpenseCategory, 'id'>>(
    expenseCategoryCollection: Type[],
    ...expenseCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseCategories: Type[] = expenseCategoriesToCheck.filter(isPresent);
    if (expenseCategories.length > 0) {
      const expenseCategoryCollectionIdentifiers = expenseCategoryCollection.map(expenseCategoryItem =>
        this.getExpenseCategoryIdentifier(expenseCategoryItem),
      );
      const expenseCategoriesToAdd = expenseCategories.filter(expenseCategoryItem => {
        const expenseCategoryIdentifier = this.getExpenseCategoryIdentifier(expenseCategoryItem);
        if (expenseCategoryCollectionIdentifiers.includes(expenseCategoryIdentifier)) {
          return false;
        }
        expenseCategoryCollectionIdentifiers.push(expenseCategoryIdentifier);
        return true;
      });
      return [...expenseCategoriesToAdd, ...expenseCategoryCollection];
    }
    return expenseCategoryCollection;
  }

  protected convertDateFromClient<T extends IExpenseCategory | NewExpenseCategory | PartialUpdateExpenseCategory>(
    expenseCategory: T,
  ): RestOf<T> {
    return {
      ...expenseCategory,
      createdDate: expenseCategory.createdDate?.toJSON() ?? null,
      lastModifiedDate: expenseCategory.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExpenseCategory: RestExpenseCategory): IExpenseCategory {
    return {
      ...restExpenseCategory,
      createdDate: restExpenseCategory.createdDate ? dayjs(restExpenseCategory.createdDate) : undefined,
      lastModifiedDate: restExpenseCategory.lastModifiedDate ? dayjs(restExpenseCategory.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExpenseCategory>): HttpResponse<IExpenseCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExpenseCategory[]>): HttpResponse<IExpenseCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
