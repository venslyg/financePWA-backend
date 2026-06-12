import dayjs from 'dayjs/esm';

import { ILiabilityLog, NewLiabilityLog } from './liability-log.model';

export const sampleWithRequiredData: ILiabilityLog = {
  id: 3321,
};

export const sampleWithPartialData: ILiabilityLog = {
  id: 27714,
  branchCode: 'breastplate shakily',
  branchId: 'lanky',
  loanFrom: 'overdub shanghai',
  liabilityType: 'SHORT_TERM',
  totalLoanAmount: 10497.64,
  endDate: dayjs('2026-06-12'),
  principalPaid: 8529.52,
  status: 'APPROVED',
};

export const sampleWithFullData: ILiabilityLog = {
  id: 13334,
  branchCode: 'whispered oblong',
  branchId: 'equal lest',
  liabilityCode: 'what',
  loanFrom: 'meh commercial yum',
  description: 'huzzah',
  liabilityType: 'SHORT_TERM',
  totalLoanAmount: 27953.6,
  startDate: dayjs('2026-06-11'),
  endDate: dayjs('2026-06-12'),
  interestPercentage: 16596.26,
  monthlyPaymentAmount: 18507.62,
  principalPaid: 19702.11,
  balanceToPay: 10725.1,
  status: 'FAILED',
  createdBy: 'flight',
  createdDate: dayjs('2026-06-12T05:07'),
  lastModifiedBy: 'question stint burly',
  lastModifiedDate: dayjs('2026-06-11T22:36'),
};

export const sampleWithNewData: NewLiabilityLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
