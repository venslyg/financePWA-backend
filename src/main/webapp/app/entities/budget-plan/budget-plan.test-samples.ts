import dayjs from 'dayjs/esm';

import { IBudgetPlan, NewBudgetPlan } from './budget-plan.model';

export const sampleWithRequiredData: IBudgetPlan = {
  id: 21107,
};

export const sampleWithPartialData: IBudgetPlan = {
  id: 7281,
  branchCode: 'kindly',
  budgetPlanCode: 'since aw',
  departmentName: 'but that amidst',
  remainingAmount: 30905.16,
  usedPercentage: 20414.1,
  createdBy: 'consequently opposite',
  lastModifiedDate: dayjs('2026-06-11T09:23'),
};

export const sampleWithFullData: IBudgetPlan = {
  id: 12532,
  branchCode: 'pillow upon',
  accountCode: 'yet knowledgeably hm',
  budgetPlanCode: 'phooey',
  departmentName: 'until',
  year: 9240,
  allocatedAmount: 27519.28,
  spentAmount: 26466.18,
  remainingAmount: 28569.16,
  usedPercentage: 24350.86,
  alertStatus: 'WARNING_80_PERCENT',
  createdBy: 'silently not',
  createdDate: dayjs('2026-06-12T00:01'),
  lastModifiedBy: 'although',
  lastModifiedDate: dayjs('2026-06-11T13:49'),
};

export const sampleWithNewData: NewBudgetPlan = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
