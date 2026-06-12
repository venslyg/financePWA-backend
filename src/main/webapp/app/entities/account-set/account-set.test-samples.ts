import dayjs from 'dayjs/esm';

import { IAccountSet, NewAccountSet } from './account-set.model';

export const sampleWithRequiredData: IAccountSet = {
  id: 2471,
};

export const sampleWithPartialData: IAccountSet = {
  id: 2064,
  branchCode: 'secret downshift whose',
  accountCode: 'where even',
  accountName: 'Money Market Account',
  createdDate: dayjs('2026-06-12T02:19'),
  lastModifiedBy: 'coaxingly doing hospitalization',
};

export const sampleWithFullData: IAccountSet = {
  id: 31489,
  branchCode: 'ouch whether ectoderm',
  accountCode: 'although',
  accountName: 'Checking Account',
  accountType: 'LIABILITY',
  subCategory: 'regal aw',
  remark: 'pish during forswear',
  createdBy: 'why pish',
  createdDate: dayjs('2026-06-11T15:13'),
  lastModifiedBy: 'hm far',
  lastModifiedDate: dayjs('2026-06-11T19:00'),
};

export const sampleWithNewData: NewAccountSet = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
