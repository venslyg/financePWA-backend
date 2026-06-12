import dayjs from 'dayjs/esm';

import { IAssetCategory, NewAssetCategory } from './asset-category.model';

export const sampleWithRequiredData: IAssetCategory = {
  id: 22266,
};

export const sampleWithPartialData: IAssetCategory = {
  id: 23907,
  assetCategoryName: 'over scholarship joyfully',
  createdDate: dayjs('2026-06-11T15:44'),
  lastModifiedBy: 'utterly recklessly pish',
};

export const sampleWithFullData: IAssetCategory = {
  id: 6412,
  assetCategoryCode: 'via',
  assetCategoryName: 'monster',
  description: 'fork and',
  createdBy: 'although whoa',
  createdDate: dayjs('2026-06-11T18:57'),
  lastModifiedBy: 'inasmuch meh casement',
  lastModifiedDate: dayjs('2026-06-11T21:33'),
};

export const sampleWithNewData: NewAssetCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
