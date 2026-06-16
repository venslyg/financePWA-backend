import dayjs from 'dayjs/esm';

import { IAssetSubCategory, NewAssetSubCategory } from './asset-sub-category.model';

export const sampleWithRequiredData: IAssetSubCategory = {
  id: 26325,
};

export const sampleWithPartialData: IAssetSubCategory = {
  id: 19901,
  branchId: 'coolly',
  assetCategoryCode: 'unexpectedly',
  assetSubCategoryName: 'abaft',
  isActive: false,
};

export const sampleWithFullData: IAssetSubCategory = {
  id: 14689,
  branchCode: 'ouch',
  branchId: 'interestingly kindly phew',
  assetCategoryCode: 'which whoever around',
  assetSubCategoryCode: 'swanling foodstuffs vastly',
  assetSubCategoryName: 'internal',
  isActive: false,
  createdBy: 'how boo',
  createdDate: dayjs('2026-06-11T14:12'),
  lastModifiedBy: 'concerning unlike',
  lastModifiedDate: dayjs('2026-06-11T12:26'),
};

export const sampleWithNewData: NewAssetSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
