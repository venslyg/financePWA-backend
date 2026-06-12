import dayjs from 'dayjs/esm';

import { IBankLedger, NewBankLedger } from './bank-ledger.model';

export const sampleWithRequiredData: IBankLedger = {
  id: 5040,
};

export const sampleWithPartialData: IBankLedger = {
  id: 18632,
  branchCode: 'however all',
  referenceNo: 'midst finally',
  depositAmount: 22555.54,
  createdBy: 'sightseeing how above',
  createdDate: dayjs('2026-06-11T10:26'),
  lastModifiedBy: 'wide-eyed excitedly',
};

export const sampleWithFullData: IBankLedger = {
  id: 3712,
  branchCode: 'weird including',
  bankLedgerCode: 'faithfully',
  date: dayjs('2026-06-11'),
  referenceNo: 'optimistically',
  description: 'pricey',
  depositAmount: 30269.96,
  withdrawalAmount: 18731.75,
  runningBalance: 6132.95,
  remark: 'waltz closely wherever',
  createdBy: 'underneath cantaloupe bah',
  createdDate: dayjs('2026-06-12T02:20'),
  lastModifiedBy: 'after grumpy eek',
  lastModifiedDate: dayjs('2026-06-11T21:59'),
};

export const sampleWithNewData: NewBankLedger = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
