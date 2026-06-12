import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountSet } from '../account-set.model';
import { AccountSetService } from '../service/account-set.service';

const accountSetResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountSet> => {
  const id = route.params.id;
  if (id) {
    return inject(AccountSetService)
      .find(id)
      .pipe(
        mergeMap((accountSet: HttpResponse<IAccountSet>) => {
          if (accountSet.body) {
            return of(accountSet.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default accountSetResolve;
