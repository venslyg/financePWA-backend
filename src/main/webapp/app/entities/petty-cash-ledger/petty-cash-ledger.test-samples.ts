import dayjs from 'dayjs/esm';

import { IPettyCashLedger, NewPettyCashLedger } from './petty-cash-ledger.model';

export const sampleWithRequiredData: IPettyCashLedger = {
  id: 10941,
};

export const sampleWithPartialData: IPettyCashLedger = {
  id: 10204,
  branchCode: 'toward abscond consequently',
  branchId: 'though',
  pettyCashCode: 'disgorge',
  date: dayjs('2026-06-12'),
  referenceNo: 'rise',
  createdBy: 'wafer zowie',
  createdDate: dayjs('2026-06-11T09:13'),
  lastModifiedBy: 'more comestible gripper',
  lastModifiedDate: dayjs('2026-06-11T23:35'),
};

export const sampleWithFullData: IPettyCashLedger = {
  id: 22881,
  branchCode: 'trench fabricate whispered',
  branchId: 'knowingly appertain outlandish',
  pettyCashCode: 'simplistic retool',
  date: dayjs('2026-06-11'),
  pettyCashVoucherNo: 'and short meh',
  description: 'after',
  cashIn: 14870.03,
  cashOut: 5349.4,
  runningBalance: 30999.68,
  linkedAccountCode: 'ew up rot',
  referenceNo: 'physical',
  createdBy: 'gloss triumphantly',
  createdDate: dayjs('2026-06-11T11:16'),
  lastModifiedBy: 'pure',
  lastModifiedDate: dayjs('2026-06-12T05:59'),
};

export const sampleWithNewData: NewPettyCashLedger = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
