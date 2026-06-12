import dayjs from 'dayjs/esm';

import { IBranch, NewBranch } from './branch.model';

export const sampleWithRequiredData: IBranch = {
  id: 3155,
};

export const sampleWithPartialData: IBranch = {
  id: 21433,
  branchCode: 'however',
  branchName: 'provided',
  isActive: true,
  createdBy: 'yet',
  createdDate: dayjs('2026-06-11T14:13'),
};

export const sampleWithFullData: IBranch = {
  id: 25059,
  branchCode: 'whoever first',
  branchName: 'at furthermore',
  location: 'what federate yum',
  phoneNumber: 'pasta uh-huh ectoderm',
  isActive: false,
  createdBy: 'upon while shoulder',
  createdDate: dayjs('2026-06-11T20:26'),
  lastModifiedBy: 'fireplace self-assured',
  lastModifiedDate: dayjs('2026-06-11T23:00'),
};

export const sampleWithNewData: NewBranch = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
