import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BankLedgerResolve from './route/bank-ledger-routing-resolve.service';

const bankLedgerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-ledger.component').then(m => m.BankLedgerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-ledger-detail.component').then(m => m.BankLedgerDetailComponent),
    resolve: {
      bankLedger: BankLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-ledger-update.component').then(m => m.BankLedgerUpdateComponent),
    resolve: {
      bankLedger: BankLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-ledger-update.component').then(m => m.BankLedgerUpdateComponent),
    resolve: {
      bankLedger: BankLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankLedgerRoute;
