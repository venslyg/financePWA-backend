import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBinCardLine } from '../bin-card-line.model';
import { BinCardLineService } from '../service/bin-card-line.service';

const binCardLineResolve = (route: ActivatedRouteSnapshot): Observable<null | IBinCardLine> => {
  const id = route.params.id;
  if (id) {
    return inject(BinCardLineService)
      .find(id)
      .pipe(
        mergeMap((binCardLine: HttpResponse<IBinCardLine>) => {
          if (binCardLine.body) {
            return of(binCardLine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default binCardLineResolve;
