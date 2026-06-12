import dayjs from 'dayjs/esm';

export interface IAssetRegister {
  id: number;
  branchCode?: string | null;
  assetRegisterCode?: string | null;
  assetCategoryCode?: string | null;
  assetSubCategoryCode?: string | null;
  assetName?: string | null;
  category?: string | null;
  purchaseDate?: dayjs.Dayjs | null;
  purchaseCost?: number | null;
  currentValue?: number | null;
  depreciationRate?: number | null;
  accumulatedDepreciation?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewAssetRegister = Omit<IAssetRegister, 'id'> & { id: null };
