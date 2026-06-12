import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBudgetPlan } from '../budget-plan.model';
import { BudgetPlanService } from '../service/budget-plan.service';

const budgetPlanResolve = (route: ActivatedRouteSnapshot): Observable<null | IBudgetPlan> => {
  const id = route.params.id;
  if (id) {
    return inject(BudgetPlanService)
      .find(id)
      .pipe(
        mergeMap((budgetPlan: HttpResponse<IBudgetPlan>) => {
          if (budgetPlan.body) {
            return of(budgetPlan.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default budgetPlanResolve;
