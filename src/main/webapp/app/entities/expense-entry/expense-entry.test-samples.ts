import dayjs from 'dayjs/esm';

import { IExpenseEntry, NewExpenseEntry } from './expense-entry.model';

export const sampleWithRequiredData: IExpenseEntry = {
  id: 5663,
};

export const sampleWithPartialData: IExpenseEntry = {
  id: 20248,
  branchId: 'pish beneath',
  accountCode: 'suburban',
  expenseCode: 'plus needy',
  date: dayjs('2026-06-11'),
  description: 'brr fledgling unlawful',
  amount: 1250.36,
  approvedBy: 'wherever',
  vendor: 'gee',
  createdBy: 'the huge',
  createdDate: dayjs('2026-06-11T16:31'),
  lastModifiedDate: dayjs('2026-06-12T06:10'),
};

export const sampleWithFullData: IExpenseEntry = {
  id: 21587,
  branchCode: 'ha',
  branchId: 'ha amidst while',
  accountCode: 'boo mmm ah',
  expenseCode: 'proliferate',
  expenseCategoryCode: 'till',
  expenseSubCategoryCode: 'why',
  createdByUsername: 'blah ick',
  date: dayjs('2026-06-12'),
  voucherNo: 'bleakly',
  description: 'valiantly',
  amount: 3115.2,
  paymentMode: 'CASH',
  approvalStatus: 'TO_REVIEW',
  approvedBy: 'yuck regarding',
  vendor: 'vacation as',
  syncStatus: 'PENDING_OFFLINE',
  createdBy: 'offset underneath',
  createdDate: dayjs('2026-06-11T23:08'),
  lastModifiedBy: 'than besides',
  lastModifiedDate: dayjs('2026-06-11T16:27'),
};

export const sampleWithNewData: NewExpenseEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
