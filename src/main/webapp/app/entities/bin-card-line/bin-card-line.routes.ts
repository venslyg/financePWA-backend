import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BinCardLineResolve from './route/bin-card-line-routing-resolve.service';

const binCardLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bin-card-line.component').then(m => m.BinCardLineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bin-card-line-detail.component').then(m => m.BinCardLineDetailComponent),
    resolve: {
      binCardLine: BinCardLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bin-card-line-update.component').then(m => m.BinCardLineUpdateComponent),
    resolve: {
      binCardLine: BinCardLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bin-card-line-update.component').then(m => m.BinCardLineUpdateComponent),
    resolve: {
      binCardLine: BinCardLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default binCardLineRoute;
