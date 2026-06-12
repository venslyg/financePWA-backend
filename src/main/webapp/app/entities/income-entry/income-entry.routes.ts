import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IncomeEntryResolve from './route/income-entry-routing-resolve.service';

const incomeEntryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/income-entry.component').then(m => m.IncomeEntryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/income-entry-detail.component').then(m => m.IncomeEntryDetailComponent),
    resolve: {
      incomeEntry: IncomeEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/income-entry-update.component').then(m => m.IncomeEntryUpdateComponent),
    resolve: {
      incomeEntry: IncomeEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/income-entry-update.component').then(m => m.IncomeEntryUpdateComponent),
    resolve: {
      incomeEntry: IncomeEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default incomeEntryRoute;
