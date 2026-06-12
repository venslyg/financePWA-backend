import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AssetCategoryResolve from './route/asset-category-routing-resolve.service';

const assetCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asset-category.component').then(m => m.AssetCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/asset-category-detail.component').then(m => m.AssetCategoryDetailComponent),
    resolve: {
      assetCategory: AssetCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/asset-category-update.component').then(m => m.AssetCategoryUpdateComponent),
    resolve: {
      assetCategory: AssetCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/asset-category-update.component').then(m => m.AssetCategoryUpdateComponent),
    resolve: {
      assetCategory: AssetCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetCategoryRoute;
