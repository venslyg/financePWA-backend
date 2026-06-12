import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILiabilityLog } from '../liability-log.model';
import { LiabilityLogService } from '../service/liability-log.service';

const liabilityLogResolve = (route: ActivatedRouteSnapshot): Observable<null | ILiabilityLog> => {
  const id = route.params.id;
  if (id) {
    return inject(LiabilityLogService)
      .find(id)
      .pipe(
        mergeMap((liabilityLog: HttpResponse<ILiabilityLog>) => {
          if (liabilityLog.body) {
            return of(liabilityLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default liabilityLogResolve;
