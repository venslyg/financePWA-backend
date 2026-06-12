import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BudgetPlanResolve from './route/budget-plan-routing-resolve.service';

const budgetPlanRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/budget-plan.component').then(m => m.BudgetPlanComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/budget-plan-detail.component').then(m => m.BudgetPlanDetailComponent),
    resolve: {
      budgetPlan: BudgetPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/budget-plan-update.component').then(m => m.BudgetPlanUpdateComponent),
    resolve: {
      budgetPlan: BudgetPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/budget-plan-update.component').then(m => m.BudgetPlanUpdateComponent),
    resolve: {
      budgetPlan: BudgetPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default budgetPlanRoute;
