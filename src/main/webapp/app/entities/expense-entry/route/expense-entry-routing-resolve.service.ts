import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseEntry } from '../expense-entry.model';
import { ExpenseEntryService } from '../service/expense-entry.service';

const expenseEntryResolve = (route: ActivatedRouteSnapshot): Observable<null | IExpenseEntry> => {
  const id = route.params.id;
  if (id) {
    return inject(ExpenseEntryService)
      .find(id)
      .pipe(
        mergeMap((expenseEntry: HttpResponse<IExpenseEntry>) => {
          if (expenseEntry.body) {
            return of(expenseEntry.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default expenseEntryResolve;
