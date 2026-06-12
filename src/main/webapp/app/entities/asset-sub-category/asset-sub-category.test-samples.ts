import dayjs from 'dayjs/esm';

import { IAssetSubCategory, NewAssetSubCategory } from './asset-sub-category.model';

export const sampleWithRequiredData: IAssetSubCategory = {
  id: 26325,
};

export const sampleWithPartialData: IAssetSubCategory = {
  id: 19764,
  assetSubCategoryCode: 'perfection pish provided',
  assetSubCategoryName: 'underachieve uniform awful',
  createdDate: dayjs('2026-06-11T21:09'),
  lastModifiedBy: 'boggle utilized',
};

export const sampleWithFullData: IAssetSubCategory = {
  id: 14689,
  assetCategoryCode: 'ouch',
  assetSubCategoryCode: 'interestingly kindly phew',
  assetSubCategoryName: 'which whoever around',
  createdBy: 'swanling foodstuffs vastly',
  createdDate: dayjs('2026-06-11T10:45'),
  lastModifiedBy: 'quickly vice whenever',
  lastModifiedDate: dayjs('2026-06-11T09:13'),
};

export const sampleWithNewData: NewAssetSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
