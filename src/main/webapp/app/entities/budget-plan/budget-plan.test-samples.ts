import dayjs from 'dayjs/esm';

import { IBudgetPlan, NewBudgetPlan } from './budget-plan.model';

export const sampleWithRequiredData: IBudgetPlan = {
  id: 21107,
};

export const sampleWithPartialData: IBudgetPlan = {
  id: 10325,
  branchCode: 'though since',
  accountCode: 'pillow but',
  budgetPlanCode: 'geez uncork',
  spentAmount: 20414.1,
  remainingAmount: 16807.5,
  alertStatus: 'RED_ALERT_100_PERCENT',
  lastModifiedBy: 'silently',
  lastModifiedDate: dayjs('2026-06-12T00:10'),
};

export const sampleWithFullData: IBudgetPlan = {
  id: 12532,
  branchCode: 'pillow upon',
  branchId: 'yet knowledgeably hm',
  accountCode: 'phooey',
  budgetPlanCode: 'until',
  departmentName: 'yum',
  year: 27574,
  allocatedAmount: 13538.34,
  spentAmount: 156.53,
  remainingAmount: 23818.03,
  usedPercentage: 2102.09,
  alertStatus: 'RED_ALERT_100_PERCENT',
  createdBy: 'printer',
  createdDate: dayjs('2026-06-11T15:03'),
  lastModifiedBy: 'emerge folklore',
  lastModifiedDate: dayjs('2026-06-12T04:40'),
};

export const sampleWithNewData: NewBudgetPlan = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
