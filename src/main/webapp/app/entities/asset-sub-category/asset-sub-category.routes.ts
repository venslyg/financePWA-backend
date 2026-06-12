import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AssetSubCategoryResolve from './route/asset-sub-category-routing-resolve.service';

const assetSubCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asset-sub-category.component').then(m => m.AssetSubCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/asset-sub-category-detail.component').then(m => m.AssetSubCategoryDetailComponent),
    resolve: {
      assetSubCategory: AssetSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/asset-sub-category-update.component').then(m => m.AssetSubCategoryUpdateComponent),
    resolve: {
      assetSubCategory: AssetSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/asset-sub-category-update.component').then(m => m.AssetSubCategoryUpdateComponent),
    resolve: {
      assetSubCategory: AssetSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetSubCategoryRoute;
