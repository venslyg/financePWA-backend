import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DonationTrackerResolve from './route/donation-tracker-routing-resolve.service';

const donationTrackerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/donation-tracker.component').then(m => m.DonationTrackerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/donation-tracker-detail.component').then(m => m.DonationTrackerDetailComponent),
    resolve: {
      donationTracker: DonationTrackerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/donation-tracker-update.component').then(m => m.DonationTrackerUpdateComponent),
    resolve: {
      donationTracker: DonationTrackerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/donation-tracker-update.component').then(m => m.DonationTrackerUpdateComponent),
    resolve: {
      donationTracker: DonationTrackerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default donationTrackerRoute;
