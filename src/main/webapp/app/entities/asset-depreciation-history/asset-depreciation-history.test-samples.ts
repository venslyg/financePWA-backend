import dayjs from 'dayjs/esm';

import { IAssetDepreciationHistory, NewAssetDepreciationHistory } from './asset-depreciation-history.model';

export const sampleWithRequiredData: IAssetDepreciationHistory = {
  id: 18478,
};

export const sampleWithPartialData: IAssetDepreciationHistory = {
  id: 13954,
  assetRegisterCode: 'boyfriend',
  depreciationAmount: 17496.22,
  processedBy: 'though',
  createdBy: 'wobbly shakily aw',
  lastModifiedDate: dayjs('2026-06-11T14:16'),
};

export const sampleWithFullData: IAssetDepreciationHistory = {
  id: 29618,
  assetRegisterCode: 'ecliptic hm',
  depreciationDate: dayjs('2026-06-12'),
  depreciationAmount: 29655.17,
  valueAfterDepreciation: 26441.03,
  processedBy: 'yippee astride appreciate',
  createdBy: 'since until',
  createdDate: dayjs('2026-06-12T04:33'),
  lastModifiedBy: 'that',
  lastModifiedDate: dayjs('2026-06-11T17:26'),
};

export const sampleWithNewData: NewAssetDepreciationHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
