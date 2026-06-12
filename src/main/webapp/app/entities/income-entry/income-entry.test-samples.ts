import dayjs from 'dayjs/esm';

import { IIncomeEntry, NewIncomeEntry } from './income-entry.model';

export const sampleWithRequiredData: IIncomeEntry = {
  id: 22947,
};

export const sampleWithPartialData: IIncomeEntry = {
  id: 9986,
  branchCode: 'impure along',
  branchId: 'free masterpiece',
  createdByUsername: 'indolent yowza',
  description: 'scrap microblog prohibition',
  incomeType: 'REGULAR',
  amount: 15997.12,
  receivablePerson: 'painfully shadowy',
  createdBy: 'engender',
  lastModifiedBy: 'intensely boldly transom',
  lastModifiedDate: dayjs('2026-06-12T03:55'),
};

export const sampleWithFullData: IIncomeEntry = {
  id: 17073,
  branchCode: 'culture entire forenenst',
  branchId: 'because poppy',
  accountCode: 'unwritten',
  incomeCode: 'rectangular out',
  createdByUsername: 'barring venture long',
  date: dayjs('2026-06-11'),
  receiptNo: 'who after majestically',
  description: 'possible grandpa',
  incomeType: 'REGULAR',
  amount: 29079.98,
  paymentMethod: 'CARD',
  receivablePerson: 'micromanage that',
  receivedBy: 'firm pish',
  syncStatus: 'PENDING_OFFLINE',
  createdBy: 'probate',
  createdDate: dayjs('2026-06-11T13:36'),
  lastModifiedBy: 'ew',
  lastModifiedDate: dayjs('2026-06-11T21:50'),
};

export const sampleWithNewData: NewIncomeEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
