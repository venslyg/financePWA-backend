import dayjs from 'dayjs/esm';

import { ILiabilityLog, NewLiabilityLog } from './liability-log.model';

export const sampleWithRequiredData: ILiabilityLog = {
  id: 3321,
};

export const sampleWithPartialData: ILiabilityLog = {
  id: 30754,
  branchCode: 'grandson whoa teammate',
  liabilityCode: 'retrospectivity govern attend',
  description: 'successfully',
  totalLoanAmount: 2356.86,
  startDate: dayjs('2026-06-12'),
  interestPercentage: 11185.64,
  balanceToPay: 25825.23,
  createdBy: 'cheap small below',
};

export const sampleWithFullData: ILiabilityLog = {
  id: 13334,
  branchCode: 'whispered oblong',
  liabilityCode: 'equal lest',
  loanFrom: 'what',
  description: 'meh commercial yum',
  liabilityType: 'LONG_TERM',
  totalLoanAmount: 32219.63,
  startDate: dayjs('2026-06-11'),
  endDate: dayjs('2026-06-11'),
  interestPercentage: 20972.21,
  monthlyPaymentAmount: 12075.09,
  principalPaid: 8227.32,
  balanceToPay: 5452.39,
  status: 'TO_REVIEW',
  createdBy: 'split inside incomplete',
  createdDate: dayjs('2026-06-11T11:08'),
  lastModifiedBy: 'aha wrong chap',
  lastModifiedDate: dayjs('2026-06-12T03:03'),
};

export const sampleWithNewData: NewLiabilityLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
