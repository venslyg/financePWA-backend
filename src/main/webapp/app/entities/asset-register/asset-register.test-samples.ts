import dayjs from 'dayjs/esm';

import { IAssetRegister, NewAssetRegister } from './asset-register.model';

export const sampleWithRequiredData: IAssetRegister = {
  id: 9229,
};

export const sampleWithPartialData: IAssetRegister = {
  id: 4659,
  assetSubCategoryCode: 'whoa',
  assetName: 'who where',
  purchaseCost: 19778.02,
  currentValue: 18512.45,
  accumulatedDepreciation: 26909.55,
  createdBy: 'phew mockingly',
  createdDate: dayjs('2026-06-12T01:07'),
  lastModifiedBy: 'um behold astride',
  lastModifiedDate: dayjs('2026-06-11T17:47'),
};

export const sampleWithFullData: IAssetRegister = {
  id: 5394,
  branchCode: 'culture hence',
  assetRegisterCode: 'nun hm ouch',
  assetCategoryCode: 'ah gee stale',
  assetSubCategoryCode: 'hastily aside phony',
  assetName: 'bah waterspout',
  category: 'yahoo starch',
  purchaseDate: dayjs('2026-06-12'),
  purchaseCost: 23039.89,
  currentValue: 1704.07,
  depreciationRate: 6141.83,
  accumulatedDepreciation: 22576.08,
  createdBy: 'never entwine slowly',
  createdDate: dayjs('2026-06-12T02:24'),
  lastModifiedBy: 'whose',
  lastModifiedDate: dayjs('2026-06-11T16:19'),
};

export const sampleWithNewData: NewAssetRegister = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
