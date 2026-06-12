import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChurchStaff } from '../church-staff.model';
import { ChurchStaffService } from '../service/church-staff.service';

const churchStaffResolve = (route: ActivatedRouteSnapshot): Observable<null | IChurchStaff> => {
  const id = route.params.id;
  if (id) {
    return inject(ChurchStaffService)
      .find(id)
      .pipe(
        mergeMap((churchStaff: HttpResponse<IChurchStaff>) => {
          if (churchStaff.body) {
            return of(churchStaff.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default churchStaffResolve;
