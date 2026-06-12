import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPettyCashLedger } from '../petty-cash-ledger.model';
import { PettyCashLedgerService } from '../service/petty-cash-ledger.service';

const pettyCashLedgerResolve = (route: ActivatedRouteSnapshot): Observable<null | IPettyCashLedger> => {
  const id = route.params.id;
  if (id) {
    return inject(PettyCashLedgerService)
      .find(id)
      .pipe(
        mergeMap((pettyCashLedger: HttpResponse<IPettyCashLedger>) => {
          if (pettyCashLedger.body) {
            return of(pettyCashLedger.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pettyCashLedgerResolve;
