import dayjs from 'dayjs/esm';

import { ISalaryPayout, NewSalaryPayout } from './salary-payout.model';

export const sampleWithRequiredData: ISalaryPayout = {
  id: 3139,
};

export const sampleWithPartialData: ISalaryPayout = {
  id: 7632,
  branchCode: 'as',
  salaryPayoutCode: 'once obedience council',
  staffCode: 'eventually folklore skyline',
  payPeriod: 'appropriate on mixed',
  allowances: 31131.9,
  deductions: 31614.23,
  netPay: 20819.9,
  payoutDate: dayjs('2026-06-11'),
  createdBy: 'pace',
  createdDate: dayjs('2026-06-12T05:57'),
};

export const sampleWithFullData: ISalaryPayout = {
  id: 22228,
  branchCode: 'how above',
  salaryPayoutCode: 'though meanwhile',
  staffCode: 'apropos scarper judgementally',
  payPeriod: 'phooey wonderfully',
  baseSalary: 17310.52,
  allowances: 7025.72,
  deductions: 19626.92,
  netPay: 16085.35,
  payoutDate: dayjs('2026-06-12'),
  createdBy: 'gee',
  createdDate: dayjs('2026-06-12T03:01'),
  lastModifiedBy: 'feline ack',
  lastModifiedDate: dayjs('2026-06-11T19:16'),
};

export const sampleWithNewData: NewSalaryPayout = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
