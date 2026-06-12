import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ChurchStaffResolve from './route/church-staff-routing-resolve.service';

const churchStaffRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/church-staff.component').then(m => m.ChurchStaffComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/church-staff-detail.component').then(m => m.ChurchStaffDetailComponent),
    resolve: {
      churchStaff: ChurchStaffResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/church-staff-update.component').then(m => m.ChurchStaffUpdateComponent),
    resolve: {
      churchStaff: ChurchStaffResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/church-staff-update.component').then(m => m.ChurchStaffUpdateComponent),
    resolve: {
      churchStaff: ChurchStaffResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default churchStaffRoute;
