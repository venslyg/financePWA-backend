import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LiabilityLogResolve from './route/liability-log-routing-resolve.service';

const liabilityLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/liability-log.component').then(m => m.LiabilityLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/liability-log-detail.component').then(m => m.LiabilityLogDetailComponent),
    resolve: {
      liabilityLog: LiabilityLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/liability-log-update.component').then(m => m.LiabilityLogUpdateComponent),
    resolve: {
      liabilityLog: LiabilityLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/liability-log-update.component').then(m => m.LiabilityLogUpdateComponent),
    resolve: {
      liabilityLog: LiabilityLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default liabilityLogRoute;
