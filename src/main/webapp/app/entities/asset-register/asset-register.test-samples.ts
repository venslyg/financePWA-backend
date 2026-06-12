import dayjs from 'dayjs/esm';

import { IAssetRegister, NewAssetRegister } from './asset-register.model';

export const sampleWithRequiredData: IAssetRegister = {
  id: 9229,
};

export const sampleWithPartialData: IAssetRegister = {
  id: 1219,
  assetCategoryCode: 'though',
  assetSubCategoryCode: 'pish next lively',
  purchaseDate: dayjs('2026-06-12'),
  purchaseCost: 10171.07,
  depreciationRate: 25392.66,
  accumulatedDepreciation: 26345.86,
  createdBy: 'hmph um behold',
  createdDate: dayjs('2026-06-12T06:03'),
  lastModifiedBy: 'optimal pfft',
  lastModifiedDate: dayjs('2026-06-12T01:33'),
};

export const sampleWithFullData: IAssetRegister = {
  id: 5394,
  branchCode: 'culture hence',
  branchId: 'nun hm ouch',
  assetRegisterCode: 'ah gee stale',
  assetCategoryCode: 'hastily aside phony',
  assetSubCategoryCode: 'bah waterspout',
  assetName: 'yahoo starch',
  category: 'slip because phooey',
  purchaseDate: dayjs('2026-06-11'),
  purchaseCost: 29441.67,
  currentValue: 10512.95,
  depreciationRate: 16145.49,
  accumulatedDepreciation: 24423.04,
  createdBy: 'phooey pace unless',
  createdDate: dayjs('2026-06-11T12:10'),
  lastModifiedBy: 'than diver',
  lastModifiedDate: dayjs('2026-06-11T11:45'),
};

export const sampleWithNewData: NewAssetRegister = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
