import dayjs from 'dayjs/esm';

export interface IAssetDepreciationHistory {
  id: number;
  assetRegisterCode?: string | null;
  depreciationDate?: dayjs.Dayjs | null;
  depreciationAmount?: number | null;
  valueAfterDepreciation?: number | null;
  processedBy?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewAssetDepreciationHistory = Omit<IAssetDepreciationHistory, 'id'> & { id: null };
