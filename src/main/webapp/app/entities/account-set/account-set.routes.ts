import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AccountSetResolve from './route/account-set-routing-resolve.service';

const accountSetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/account-set.component').then(m => m.AccountSetComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/account-set-detail.component').then(m => m.AccountSetDetailComponent),
    resolve: {
      accountSet: AccountSetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/account-set-update.component').then(m => m.AccountSetUpdateComponent),
    resolve: {
      accountSet: AccountSetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/account-set-update.component').then(m => m.AccountSetUpdateComponent),
    resolve: {
      accountSet: AccountSetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountSetRoute;
