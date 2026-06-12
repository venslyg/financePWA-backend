import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncomeEntry } from '../income-entry.model';
import { IncomeEntryService } from '../service/income-entry.service';

const incomeEntryResolve = (route: ActivatedRouteSnapshot): Observable<null | IIncomeEntry> => {
  const id = route.params.id;
  if (id) {
    return inject(IncomeEntryService)
      .find(id)
      .pipe(
        mergeMap((incomeEntry: HttpResponse<IIncomeEntry>) => {
          if (incomeEntry.body) {
            return of(incomeEntry.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default incomeEntryResolve;
