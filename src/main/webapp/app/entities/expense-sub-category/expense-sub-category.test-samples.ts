import dayjs from 'dayjs/esm';

import { IExpenseSubCategory, NewExpenseSubCategory } from './expense-sub-category.model';

export const sampleWithRequiredData: IExpenseSubCategory = {
  id: 15244,
};

export const sampleWithPartialData: IExpenseSubCategory = {
  id: 9691,
  branchCode: 'once abnormally about',
  categoryCode: 'instead',
  createdBy: 'before management bossy',
  lastModifiedBy: 'claw for',
  lastModifiedDate: dayjs('2026-06-12T00:32'),
};

export const sampleWithFullData: IExpenseSubCategory = {
  id: 19834,
  branchCode: 'tag',
  branchId: 'toward worth outstanding',
  categoryCode: 'at',
  subCategoryCode: 'foot unsteady',
  subCategoryName: 'ick championship',
  createdBy: 'major',
  createdDate: dayjs('2026-06-12T01:11'),
  lastModifiedBy: 'rewarding from underneath',
  lastModifiedDate: dayjs('2026-06-11T17:47'),
};

export const sampleWithNewData: NewExpenseSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
