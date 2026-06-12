import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonationTracker } from '../donation-tracker.model';
import { DonationTrackerService } from '../service/donation-tracker.service';

const donationTrackerResolve = (route: ActivatedRouteSnapshot): Observable<null | IDonationTracker> => {
  const id = route.params.id;
  if (id) {
    return inject(DonationTrackerService)
      .find(id)
      .pipe(
        mergeMap((donationTracker: HttpResponse<IDonationTracker>) => {
          if (donationTracker.body) {
            return of(donationTracker.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default donationTrackerResolve;
