import dayjs from 'dayjs/esm';

import { IAssetSubCategory, NewAssetSubCategory } from './asset-sub-category.model';

export const sampleWithRequiredData: IAssetSubCategory = {
  id: 26325,
};

export const sampleWithPartialData: IAssetSubCategory = {
  id: 29506,
  branchId: 'in expostulate',
  assetCategoryCode: 'truthfully dishonor',
  assetSubCategoryName: 'frail orientate',
  createdBy: 'instructor mostly whack',
};

export const sampleWithFullData: IAssetSubCategory = {
  id: 14689,
  branchCode: 'ouch',
  branchId: 'interestingly kindly phew',
  assetCategoryCode: 'which whoever around',
  assetSubCategoryCode: 'swanling foodstuffs vastly',
  assetSubCategoryName: 'internal',
  createdBy: 'vice whenever',
  createdDate: dayjs('2026-06-11T09:13'),
  lastModifiedBy: 'mountain',
  lastModifiedDate: dayjs('2026-06-11T16:02'),
};

export const sampleWithNewData: NewAssetSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
