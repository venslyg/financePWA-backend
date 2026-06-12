import dayjs from 'dayjs/esm';

import { IExpenseEntry, NewExpenseEntry } from './expense-entry.model';

export const sampleWithRequiredData: IExpenseEntry = {
  id: 5663,
};

export const sampleWithPartialData: IExpenseEntry = {
  id: 14946,
  accountCode: 'long word',
  expenseCode: 'vista',
  expenseCategoryCode: 'meaningfully quit retract',
  voucherNo: 'pfft scenario',
  amount: 22414.86,
  paymentMode: 'CARD',
  vendor: 'fairly',
  syncStatus: 'PENDING_OFFLINE',
  createdDate: dayjs('2026-06-11T11:05'),
  lastModifiedBy: 'lest the',
};

export const sampleWithFullData: IExpenseEntry = {
  id: 21587,
  branchCode: 'ha',
  accountCode: 'ha amidst while',
  expenseCode: 'boo mmm ah',
  expenseCategoryCode: 'proliferate',
  expenseSubCategoryCode: 'till',
  createdByUsername: 'why',
  date: dayjs('2026-06-11'),
  voucherNo: 'so save longingly',
  description: 'now',
  amount: 30007.46,
  paymentMode: 'CASH',
  approvalStatus: 'DECLINED',
  approvedBy: 'or instantly',
  vendor: 'however or blissfully',
  syncStatus: 'SYNCED',
  createdBy: 'spook striking bleakly',
  createdDate: dayjs('2026-06-11T08:17'),
  lastModifiedBy: 'glossy',
  lastModifiedDate: dayjs('2026-06-12T01:51'),
};

export const sampleWithNewData: NewExpenseEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
