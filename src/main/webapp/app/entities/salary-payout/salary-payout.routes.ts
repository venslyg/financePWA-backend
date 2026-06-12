import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SalaryPayoutResolve from './route/salary-payout-routing-resolve.service';

const salaryPayoutRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/salary-payout.component').then(m => m.SalaryPayoutComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/salary-payout-detail.component').then(m => m.SalaryPayoutDetailComponent),
    resolve: {
      salaryPayout: SalaryPayoutResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/salary-payout-update.component').then(m => m.SalaryPayoutUpdateComponent),
    resolve: {
      salaryPayout: SalaryPayoutResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/salary-payout-update.component').then(m => m.SalaryPayoutUpdateComponent),
    resolve: {
      salaryPayout: SalaryPayoutResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default salaryPayoutRoute;
