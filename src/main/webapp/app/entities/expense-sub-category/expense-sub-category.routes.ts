import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExpenseSubCategoryResolve from './route/expense-sub-category-routing-resolve.service';

const expenseSubCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/expense-sub-category.component').then(m => m.ExpenseSubCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/expense-sub-category-detail.component').then(m => m.ExpenseSubCategoryDetailComponent),
    resolve: {
      expenseSubCategory: ExpenseSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/expense-sub-category-update.component').then(m => m.ExpenseSubCategoryUpdateComponent),
    resolve: {
      expenseSubCategory: ExpenseSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/expense-sub-category-update.component').then(m => m.ExpenseSubCategoryUpdateComponent),
    resolve: {
      expenseSubCategory: ExpenseSubCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expenseSubCategoryRoute;
