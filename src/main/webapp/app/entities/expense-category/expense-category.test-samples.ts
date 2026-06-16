import dayjs from 'dayjs/esm';

import { IExpenseCategory, NewExpenseCategory } from './expense-category.model';

export const sampleWithRequiredData: IExpenseCategory = {
  id: 22122,
};

export const sampleWithPartialData: IExpenseCategory = {
  id: 11631,
  categoryCode: 'confusion onto',
  categoryName: 'amount word openly',
  description: 'drat meh',
  lastModifiedDate: dayjs('2026-06-11T12:22'),
};

export const sampleWithFullData: IExpenseCategory = {
  id: 3552,
  branchCode: 'strategy ceramic',
  branchId: 'pike sore exalted',
  categoryCode: 'reassuringly',
  categoryName: 'unto',
  description: 'well',
  isActive: true,
  createdBy: 'nerve coaxingly',
  createdDate: dayjs('2026-06-12T04:10'),
  lastModifiedBy: 'opposite whose',
  lastModifiedDate: dayjs('2026-06-11T15:52'),
};

export const sampleWithNewData: NewExpenseCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
