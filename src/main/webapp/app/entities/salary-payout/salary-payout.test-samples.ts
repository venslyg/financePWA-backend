import dayjs from 'dayjs/esm';

import { ISalaryPayout, NewSalaryPayout } from './salary-payout.model';

export const sampleWithRequiredData: ISalaryPayout = {
  id: 3139,
};

export const sampleWithPartialData: ISalaryPayout = {
  id: 3044,
  branchCode: 'zowie',
  branchId: 'chromakey near suddenly',
  salaryPayoutCode: 'deeply throughout',
  staffCode: 'yahoo pinion status',
  baseSalary: 27622.75,
  allowances: 9646.12,
  deductions: 15337.78,
  netPay: 29394.19,
  payoutDate: dayjs('2026-06-12'),
  createdBy: 'now squeaky',
  lastModifiedDate: dayjs('2026-06-11T20:48'),
};

export const sampleWithFullData: ISalaryPayout = {
  id: 22228,
  branchCode: 'how above',
  branchId: 'though meanwhile',
  salaryPayoutCode: 'apropos scarper judgementally',
  staffCode: 'phooey wonderfully',
  payPeriod: 'daughter inside',
  baseSalary: 6032.74,
  allowances: 25026.68,
  deductions: 26843.05,
  netPay: 18616.75,
  payoutDate: dayjs('2026-06-11'),
  createdBy: 'pish chainstay suffice',
  createdDate: dayjs('2026-06-12T04:42'),
  lastModifiedBy: 'mid instead',
  lastModifiedDate: dayjs('2026-06-11T21:49'),
};

export const sampleWithNewData: NewSalaryPayout = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
