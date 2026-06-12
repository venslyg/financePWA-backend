import dayjs from 'dayjs/esm';

import { IExpenseSubCategory, NewExpenseSubCategory } from './expense-sub-category.model';

export const sampleWithRequiredData: IExpenseSubCategory = {
  id: 22553,
};

export const sampleWithPartialData: IExpenseSubCategory = {
  id: 15244,
  categoryCode: 'orientate',
  subCategoryName: 'archaeology',
  lastModifiedBy: 'about',
};

export const sampleWithFullData: IExpenseSubCategory = {
  id: 19834,
  categoryCode: 'tag',
  subCategoryCode: 'toward worth outstanding',
  subCategoryName: 'at',
  createdBy: 'foot unsteady',
  createdDate: dayjs('2026-06-11T19:50'),
  lastModifiedBy: 'respray',
  lastModifiedDate: dayjs('2026-06-11T14:06'),
};

export const sampleWithNewData: NewExpenseSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
