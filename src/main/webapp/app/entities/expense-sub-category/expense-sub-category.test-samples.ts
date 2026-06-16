import dayjs from 'dayjs/esm';

import { IExpenseSubCategory, NewExpenseSubCategory } from './expense-sub-category.model';

export const sampleWithRequiredData: IExpenseSubCategory = {
  id: 15244,
};

export const sampleWithPartialData: IExpenseSubCategory = {
  id: 23450,
  branchCode: 'unto uselessly inasmuch',
  categoryCode: 'through',
  isActive: true,
  createdDate: dayjs('2026-06-11T09:53'),
  lastModifiedBy: 'small',
  lastModifiedDate: dayjs('2026-06-11T13:14'),
};

export const sampleWithFullData: IExpenseSubCategory = {
  id: 19834,
  branchCode: 'tag',
  branchId: 'toward worth outstanding',
  categoryCode: 'at',
  subCategoryCode: 'foot unsteady',
  subCategoryName: 'ick championship',
  isActive: true,
  createdBy: 'ouch willing',
  createdDate: dayjs('2026-06-11T23:26'),
  lastModifiedBy: 'which internalise',
  lastModifiedDate: dayjs('2026-06-11T10:37'),
};

export const sampleWithNewData: NewExpenseSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
