import dayjs from 'dayjs/esm';

import { IExpenseCategory, NewExpenseCategory } from './expense-category.model';

export const sampleWithRequiredData: IExpenseCategory = {
  id: 22122,
};

export const sampleWithPartialData: IExpenseCategory = {
  id: 22676,
  description: 'patiently furthermore orientate',
  createdBy: 'knife awful fortunate',
  createdDate: dayjs('2026-06-11T15:55'),
};

export const sampleWithFullData: IExpenseCategory = {
  id: 3552,
  categoryCode: 'strategy ceramic',
  categoryName: 'pike sore exalted',
  description: 'reassuringly',
  createdBy: 'unto',
  createdDate: dayjs('2026-06-11T12:25'),
  lastModifiedBy: 'for',
  lastModifiedDate: dayjs('2026-06-11T15:45'),
};

export const sampleWithNewData: NewExpenseCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
