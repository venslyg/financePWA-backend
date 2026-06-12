import dayjs from 'dayjs/esm';

import { IAssetDepreciationHistory, NewAssetDepreciationHistory } from './asset-depreciation-history.model';

export const sampleWithRequiredData: IAssetDepreciationHistory = {
  id: 18478,
};

export const sampleWithPartialData: IAssetDepreciationHistory = {
  id: 2200,
  branchCode: 'pfft though poor',
  assetRegisterCode: 'shakily aw competent',
  depreciationAmount: 9235.08,
  valueAfterDepreciation: 20727.67,
  createdDate: dayjs('2026-06-11T20:18'),
  lastModifiedBy: 'judgementally supposing collaboration',
  lastModifiedDate: dayjs('2026-06-11T17:44'),
};

export const sampleWithFullData: IAssetDepreciationHistory = {
  id: 29618,
  branchCode: 'ecliptic hm',
  branchId: 'blah upbeat eek',
  assetRegisterCode: 'wrongly intently',
  depreciationDate: dayjs('2026-06-11'),
  depreciationAmount: 2442.06,
  valueAfterDepreciation: 23497.91,
  processedBy: 'nor neatly safely',
  createdBy: 'pace',
  createdDate: dayjs('2026-06-11T19:44'),
  lastModifiedBy: 'given',
  lastModifiedDate: dayjs('2026-06-11T09:32'),
};

export const sampleWithNewData: NewAssetDepreciationHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
