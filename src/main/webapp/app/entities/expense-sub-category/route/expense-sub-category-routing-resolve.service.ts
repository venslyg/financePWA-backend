import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseSubCategory } from '../expense-sub-category.model';
import { ExpenseSubCategoryService } from '../service/expense-sub-category.service';

const expenseSubCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IExpenseSubCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(ExpenseSubCategoryService)
      .find(id)
      .pipe(
        mergeMap((expenseSubCategory: HttpResponse<IExpenseSubCategory>) => {
          if (expenseSubCategory.body) {
            return of(expenseSubCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default expenseSubCategoryResolve;
