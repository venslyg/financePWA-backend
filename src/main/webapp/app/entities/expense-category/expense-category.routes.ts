import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExpenseCategoryResolve from './route/expense-category-routing-resolve.service';

const expenseCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/expense-category.component').then(m => m.ExpenseCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/expense-category-detail.component').then(m => m.ExpenseCategoryDetailComponent),
    resolve: {
      expenseCategory: ExpenseCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/expense-category-update.component').then(m => m.ExpenseCategoryUpdateComponent),
    resolve: {
      expenseCategory: ExpenseCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/expense-category-update.component').then(m => m.ExpenseCategoryUpdateComponent),
    resolve: {
      expenseCategory: ExpenseCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expenseCategoryRoute;
