import dayjs from 'dayjs/esm';

import { IPettyCashLedger, NewPettyCashLedger } from './petty-cash-ledger.model';

export const sampleWithRequiredData: IPettyCashLedger = {
  id: 10941,
};

export const sampleWithPartialData: IPettyCashLedger = {
  id: 3522,
  branchCode: 'fax',
  pettyCashCode: 'abscond consequently forceful',
  date: dayjs('2026-06-11'),
  pettyCashVoucherNo: 'disgorge',
  createdBy: 'if redact uh-huh',
  createdDate: dayjs('2026-06-11T14:54'),
  lastModifiedBy: 'ad anenst after',
  lastModifiedDate: dayjs('2026-06-12T03:51'),
};

export const sampleWithFullData: IPettyCashLedger = {
  id: 22881,
  branchCode: 'trench fabricate whispered',
  pettyCashCode: 'knowingly appertain outlandish',
  date: dayjs('2026-06-11'),
  pettyCashVoucherNo: 'joyful perp cruelly',
  description: 'short',
  cashIn: 4744.9,
  cashOut: 24213.76,
  runningBalance: 12878.59,
  linkedAccountCode: 'bah',
  referenceNo: 'till',
  createdBy: 'fatally',
  createdDate: dayjs('2026-06-11T23:19'),
  lastModifiedBy: 'yet',
  lastModifiedDate: dayjs('2026-06-12T01:39'),
};

export const sampleWithNewData: NewPettyCashLedger = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
