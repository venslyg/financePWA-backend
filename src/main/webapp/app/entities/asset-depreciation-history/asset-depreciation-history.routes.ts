import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AssetDepreciationHistoryResolve from './route/asset-depreciation-history-routing-resolve.service';

const assetDepreciationHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asset-depreciation-history.component').then(m => m.AssetDepreciationHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/asset-depreciation-history-detail.component').then(m => m.AssetDepreciationHistoryDetailComponent),
    resolve: {
      assetDepreciationHistory: AssetDepreciationHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/asset-depreciation-history-update.component').then(m => m.AssetDepreciationHistoryUpdateComponent),
    resolve: {
      assetDepreciationHistory: AssetDepreciationHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/asset-depreciation-history-update.component').then(m => m.AssetDepreciationHistoryUpdateComponent),
    resolve: {
      assetDepreciationHistory: AssetDepreciationHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetDepreciationHistoryRoute;
