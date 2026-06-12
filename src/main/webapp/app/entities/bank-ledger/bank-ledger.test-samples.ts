import dayjs from 'dayjs/esm';

import { IBankLedger, NewBankLedger } from './bank-ledger.model';

export const sampleWithRequiredData: IBankLedger = {
  id: 5040,
};

export const sampleWithPartialData: IBankLedger = {
  id: 11566,
  branchCode: 'zen',
  date: dayjs('2026-06-11'),
  description: 'anti marimba tuber',
  remark: 'yowza deflect',
  createdBy: 'judgementally',
  createdDate: dayjs('2026-06-12T00:10'),
};

export const sampleWithFullData: IBankLedger = {
  id: 3712,
  branchCode: 'weird including',
  branchId: 'faithfully',
  bankLedgerCode: 'everlasting intent',
  date: dayjs('2026-06-12'),
  referenceNo: 'publication surface',
  description: 'schlep as openly',
  depositAmount: 12361.82,
  withdrawalAmount: 4759.84,
  runningBalance: 18116.09,
  remark: 'bah',
  createdBy: 'unit readmit expostulate',
  createdDate: dayjs('2026-06-11T21:38'),
  lastModifiedBy: 'kaleidoscopic always',
  lastModifiedDate: dayjs('2026-06-11T17:14'),
};

export const sampleWithNewData: NewBankLedger = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
