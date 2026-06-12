import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalaryPayout } from '../salary-payout.model';
import { SalaryPayoutService } from '../service/salary-payout.service';

const salaryPayoutResolve = (route: ActivatedRouteSnapshot): Observable<null | ISalaryPayout> => {
  const id = route.params.id;
  if (id) {
    return inject(SalaryPayoutService)
      .find(id)
      .pipe(
        mergeMap((salaryPayout: HttpResponse<ISalaryPayout>) => {
          if (salaryPayout.body) {
            return of(salaryPayout.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default salaryPayoutResolve;
