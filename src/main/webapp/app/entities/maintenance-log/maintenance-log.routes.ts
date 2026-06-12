import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaintenanceLogResolve from './route/maintenance-log-routing-resolve.service';

const maintenanceLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maintenance-log.component').then(m => m.MaintenanceLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maintenance-log-detail.component').then(m => m.MaintenanceLogDetailComponent),
    resolve: {
      maintenanceLog: MaintenanceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maintenance-log-update.component').then(m => m.MaintenanceLogUpdateComponent),
    resolve: {
      maintenanceLog: MaintenanceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maintenance-log-update.component').then(m => m.MaintenanceLogUpdateComponent),
    resolve: {
      maintenanceLog: MaintenanceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maintenanceLogRoute;
