import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'branch',
    data: { pageTitle: 'Branches' },
    loadChildren: () => import('./branch/branch.routes'),
  },
  {
    path: 'account-set',
    data: { pageTitle: 'AccountSets' },
    loadChildren: () => import('./account-set/account-set.routes'),
  },
  {
    path: 'income-entry',
    data: { pageTitle: 'IncomeEntries' },
    loadChildren: () => import('./income-entry/income-entry.routes'),
  },
  {
    path: 'expense-category',
    data: { pageTitle: 'ExpenseCategories' },
    loadChildren: () => import('./expense-category/expense-category.routes'),
  },
  {
    path: 'expense-sub-category',
    data: { pageTitle: 'ExpenseSubCategories' },
    loadChildren: () => import('./expense-sub-category/expense-sub-category.routes'),
  },
  {
    path: 'expense-entry',
    data: { pageTitle: 'ExpenseEntries' },
    loadChildren: () => import('./expense-entry/expense-entry.routes'),
  },
  {
    path: 'bank-ledger',
    data: { pageTitle: 'BankLedgers' },
    loadChildren: () => import('./bank-ledger/bank-ledger.routes'),
  },
  {
    path: 'petty-cash-ledger',
    data: { pageTitle: 'PettyCashLedgers' },
    loadChildren: () => import('./petty-cash-ledger/petty-cash-ledger.routes'),
  },
  {
    path: 'asset-category',
    data: { pageTitle: 'AssetCategories' },
    loadChildren: () => import('./asset-category/asset-category.routes'),
  },
  {
    path: 'asset-sub-category',
    data: { pageTitle: 'AssetSubCategories' },
    loadChildren: () => import('./asset-sub-category/asset-sub-category.routes'),
  },
  {
    path: 'asset-register',
    data: { pageTitle: 'AssetRegisters' },
    loadChildren: () => import('./asset-register/asset-register.routes'),
  },
  {
    path: 'asset-depreciation-history',
    data: { pageTitle: 'AssetDepreciationHistories' },
    loadChildren: () => import('./asset-depreciation-history/asset-depreciation-history.routes'),
  },
  {
    path: 'maintenance-log',
    data: { pageTitle: 'MaintenanceLogs' },
    loadChildren: () => import('./maintenance-log/maintenance-log.routes'),
  },
  {
    path: 'inventory-item',
    data: { pageTitle: 'InventoryItems' },
    loadChildren: () => import('./inventory-item/inventory-item.routes'),
  },
  {
    path: 'bin-card-line',
    data: { pageTitle: 'BinCardLines' },
    loadChildren: () => import('./bin-card-line/bin-card-line.routes'),
  },
  {
    path: 'liability-log',
    data: { pageTitle: 'LiabilityLogs' },
    loadChildren: () => import('./liability-log/liability-log.routes'),
  },
  {
    path: 'budget-plan',
    data: { pageTitle: 'BudgetPlans' },
    loadChildren: () => import('./budget-plan/budget-plan.routes'),
  },
  {
    path: 'donation-tracker',
    data: { pageTitle: 'DonationTrackers' },
    loadChildren: () => import('./donation-tracker/donation-tracker.routes'),
  },
  {
    path: 'church-staff',
    data: { pageTitle: 'ChurchStaffs' },
    loadChildren: () => import('./church-staff/church-staff.routes'),
  },
  {
    path: 'salary-payout',
    data: { pageTitle: 'SalaryPayouts' },
    loadChildren: () => import('./salary-payout/salary-payout.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
