import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBudgetPlan, NewBudgetPlan } from '../budget-plan.model';

export type PartialUpdateBudgetPlan = Partial<IBudgetPlan> & Pick<IBudgetPlan, 'id'>;

type RestOf<T extends IBudgetPlan | NewBudgetPlan> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBudgetPlan = RestOf<IBudgetPlan>;

export type NewRestBudgetPlan = RestOf<NewBudgetPlan>;

export type PartialUpdateRestBudgetPlan = RestOf<PartialUpdateBudgetPlan>;

export type EntityResponseType = HttpResponse<IBudgetPlan>;
export type EntityArrayResponseType = HttpResponse<IBudgetPlan[]>;

@Injectable({ providedIn: 'root' })
export class BudgetPlanService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/budget-plans');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/budget-plans/_search');

  create(budgetPlan: NewBudgetPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetPlan);
    return this.http
      .post<RestBudgetPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(budgetPlan: IBudgetPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetPlan);
    return this.http
      .put<RestBudgetPlan>(`${this.resourceUrl}/${this.getBudgetPlanIdentifier(budgetPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(budgetPlan: PartialUpdateBudgetPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetPlan);
    return this.http
      .patch<RestBudgetPlan>(`${this.resourceUrl}/${this.getBudgetPlanIdentifier(budgetPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBudgetPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBudgetPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBudgetPlan[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IBudgetPlan[]>()], asapScheduler)),
    );
  }

  getBudgetPlanIdentifier(budgetPlan: Pick<IBudgetPlan, 'id'>): number {
    return budgetPlan.id;
  }

  compareBudgetPlan(o1: Pick<IBudgetPlan, 'id'> | null, o2: Pick<IBudgetPlan, 'id'> | null): boolean {
    return o1 && o2 ? this.getBudgetPlanIdentifier(o1) === this.getBudgetPlanIdentifier(o2) : o1 === o2;
  }

  addBudgetPlanToCollectionIfMissing<Type extends Pick<IBudgetPlan, 'id'>>(
    budgetPlanCollection: Type[],
    ...budgetPlansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const budgetPlans: Type[] = budgetPlansToCheck.filter(isPresent);
    if (budgetPlans.length > 0) {
      const budgetPlanCollectionIdentifiers = budgetPlanCollection.map(budgetPlanItem => this.getBudgetPlanIdentifier(budgetPlanItem));
      const budgetPlansToAdd = budgetPlans.filter(budgetPlanItem => {
        const budgetPlanIdentifier = this.getBudgetPlanIdentifier(budgetPlanItem);
        if (budgetPlanCollectionIdentifiers.includes(budgetPlanIdentifier)) {
          return false;
        }
        budgetPlanCollectionIdentifiers.push(budgetPlanIdentifier);
        return true;
      });
      return [...budgetPlansToAdd, ...budgetPlanCollection];
    }
    return budgetPlanCollection;
  }

  protected convertDateFromClient<T extends IBudgetPlan | NewBudgetPlan | PartialUpdateBudgetPlan>(budgetPlan: T): RestOf<T> {
    return {
      ...budgetPlan,
      createdDate: budgetPlan.createdDate?.toJSON() ?? null,
      lastModifiedDate: budgetPlan.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBudgetPlan: RestBudgetPlan): IBudgetPlan {
    return {
      ...restBudgetPlan,
      createdDate: restBudgetPlan.createdDate ? dayjs(restBudgetPlan.createdDate) : undefined,
      lastModifiedDate: restBudgetPlan.lastModifiedDate ? dayjs(restBudgetPlan.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBudgetPlan>): HttpResponse<IBudgetPlan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBudgetPlan[]>): HttpResponse<IBudgetPlan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
