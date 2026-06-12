import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PettyCashLedgerResolve from './route/petty-cash-ledger-routing-resolve.service';

const pettyCashLedgerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/petty-cash-ledger.component').then(m => m.PettyCashLedgerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/petty-cash-ledger-detail.component').then(m => m.PettyCashLedgerDetailComponent),
    resolve: {
      pettyCashLedger: PettyCashLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/petty-cash-ledger-update.component').then(m => m.PettyCashLedgerUpdateComponent),
    resolve: {
      pettyCashLedger: PettyCashLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/petty-cash-ledger-update.component').then(m => m.PettyCashLedgerUpdateComponent),
    resolve: {
      pettyCashLedger: PettyCashLedgerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pettyCashLedgerRoute;
