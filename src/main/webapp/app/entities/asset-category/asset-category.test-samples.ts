import dayjs from 'dayjs/esm';

import { IAssetCategory, NewAssetCategory } from './asset-category.model';

export const sampleWithRequiredData: IAssetCategory = {
  id: 22266,
};

export const sampleWithPartialData: IAssetCategory = {
  id: 28857,
  branchId: 'er under',
  description: 'unwieldy technician',
  createdBy: 'per',
};

export const sampleWithFullData: IAssetCategory = {
  id: 6412,
  branchCode: 'via',
  branchId: 'monster',
  assetCategoryCode: 'fork and',
  assetCategoryName: 'although whoa',
  description: 'inasmuch rim',
  createdBy: 'casement as',
  createdDate: dayjs('2026-06-11T22:38'),
  lastModifiedBy: 'oh dissemble',
  lastModifiedDate: dayjs('2026-06-12T02:52'),
};

export const sampleWithNewData: NewAssetCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
