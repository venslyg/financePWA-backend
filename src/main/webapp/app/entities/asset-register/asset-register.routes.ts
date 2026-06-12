import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AssetRegisterResolve from './route/asset-register-routing-resolve.service';

const assetRegisterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asset-register.component').then(m => m.AssetRegisterComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/asset-register-detail.component').then(m => m.AssetRegisterDetailComponent),
    resolve: {
      assetRegister: AssetRegisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/asset-register-update.component').then(m => m.AssetRegisterUpdateComponent),
    resolve: {
      assetRegister: AssetRegisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/asset-register-update.component').then(m => m.AssetRegisterUpdateComponent),
    resolve: {
      assetRegister: AssetRegisterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetRegisterRoute;
