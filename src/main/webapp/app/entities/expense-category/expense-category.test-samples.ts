import dayjs from 'dayjs/esm';

import { IExpenseCategory, NewExpenseCategory } from './expense-category.model';

export const sampleWithRequiredData: IExpenseCategory = {
  id: 22122,
};

export const sampleWithPartialData: IExpenseCategory = {
  id: 2198,
  categoryCode: 'spectate harp',
  categoryName: 'fort imagineer',
  description: 'openly fuzzy festival',
};

export const sampleWithFullData: IExpenseCategory = {
  id: 3552,
  branchCode: 'strategy ceramic',
  branchId: 'pike sore exalted',
  categoryCode: 'reassuringly',
  categoryName: 'unto',
  description: 'well',
  createdBy: 'yowza',
  createdDate: dayjs('2026-06-11T21:28'),
  lastModifiedBy: 'upside-down mmm hmph',
  lastModifiedDate: dayjs('2026-06-12T06:00'),
};

export const sampleWithNewData: NewExpenseCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
