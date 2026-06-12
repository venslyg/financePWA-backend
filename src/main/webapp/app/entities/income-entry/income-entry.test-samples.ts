import dayjs from 'dayjs/esm';

import { IIncomeEntry, NewIncomeEntry } from './income-entry.model';

export const sampleWithRequiredData: IIncomeEntry = {
  id: 22947,
};

export const sampleWithPartialData: IIncomeEntry = {
  id: 4458,
  branchCode: 'petty',
  accountCode: 'along strident',
  date: dayjs('2026-06-11'),
  incomeType: 'REGULAR',
  amount: 31883.22,
  paymentMethod: 'CASH',
  receivedBy: 'muddy',
  createdDate: dayjs('2026-06-12T06:06'),
  lastModifiedDate: dayjs('2026-06-11T18:27'),
};

export const sampleWithFullData: IIncomeEntry = {
  id: 17073,
  branchCode: 'culture entire forenenst',
  accountCode: 'because poppy',
  incomeCode: 'unwritten',
  createdByUsername: 'rectangular out',
  date: dayjs('2026-06-11'),
  receiptNo: 'floss',
  description: 'with whoever who',
  incomeType: 'REGULAR',
  amount: 1989.03,
  paymentMethod: 'CARD',
  receivablePerson: 'plus useless evenly',
  receivedBy: 'eek micromanage that',
  syncStatus: 'PENDING_OFFLINE',
  createdBy: 'scope',
  createdDate: dayjs('2026-06-11T12:06'),
  lastModifiedBy: 'dramatize',
  lastModifiedDate: dayjs('2026-06-12T03:26'),
};

export const sampleWithNewData: NewIncomeEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
