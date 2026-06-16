import dayjs from 'dayjs/esm';

import { IAssetCategory, NewAssetCategory } from './asset-category.model';

export const sampleWithRequiredData: IAssetCategory = {
  id: 22266,
};

export const sampleWithPartialData: IAssetCategory = {
  id: 11038,
  branchId: 'yippee or',
  description: 'bad afore',
  isActive: true,
};

export const sampleWithFullData: IAssetCategory = {
  id: 6412,
  branchCode: 'via',
  branchId: 'monster',
  assetCategoryCode: 'fork and',
  assetCategoryName: 'although whoa',
  description: 'inasmuch rim',
  isActive: true,
  createdBy: 'supposing though joy',
  createdDate: dayjs('2026-06-11T07:46'),
  lastModifiedBy: 'slink',
  lastModifiedDate: dayjs('2026-06-11T21:06'),
};

export const sampleWithNewData: NewAssetCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
