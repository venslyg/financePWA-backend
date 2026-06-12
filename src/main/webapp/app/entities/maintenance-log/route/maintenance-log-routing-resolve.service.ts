import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaintenanceLog } from '../maintenance-log.model';
import { MaintenanceLogService } from '../service/maintenance-log.service';

const maintenanceLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaintenanceLog> => {
  const id = route.params.id;
  if (id) {
    return inject(MaintenanceLogService)
      .find(id)
      .pipe(
        mergeMap((maintenanceLog: HttpResponse<IMaintenanceLog>) => {
          if (maintenanceLog.body) {
            return of(maintenanceLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default maintenanceLogResolve;
