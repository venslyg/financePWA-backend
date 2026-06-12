import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBankLedger } from '../bank-ledger.model';
import { BankLedgerService } from '../service/bank-ledger.service';

const bankLedgerResolve = (route: ActivatedRouteSnapshot): Observable<null | IBankLedger> => {
  const id = route.params.id;
  if (id) {
    return inject(BankLedgerService)
      .find(id)
      .pipe(
        mergeMap((bankLedger: HttpResponse<IBankLedger>) => {
          if (bankLedger.body) {
            return of(bankLedger.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bankLedgerResolve;
