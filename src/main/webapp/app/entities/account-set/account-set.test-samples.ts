import dayjs from 'dayjs/esm';

import { IAccountSet, NewAccountSet } from './account-set.model';

export const sampleWithRequiredData: IAccountSet = {
  id: 2471,
};

export const sampleWithPartialData: IAccountSet = {
  id: 28672,
  branchCode: 'whoa absent gadzooks',
  branchId: 'illusion what',
  accountCode: 'whoa yippee uh-huh',
  createdBy: 'pleasant phew',
  createdDate: dayjs('2026-06-11T23:20'),
  lastModifiedDate: dayjs('2026-06-12T03:19'),
};

export const sampleWithFullData: IAccountSet = {
  id: 31489,
  branchCode: 'ouch whether ectoderm',
  branchId: 'although',
  accountCode: 'jet',
  accountName: 'Savings Account',
  accountType: 'EXPENSE',
  subCategory: 'severe pish during',
  remark: 'instantly',
  createdBy: 'whoa but',
  createdDate: dayjs('2026-06-11T19:21'),
  lastModifiedBy: 'that once geez',
  lastModifiedDate: dayjs('2026-06-11T08:45'),
};

export const sampleWithNewData: NewAccountSet = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
