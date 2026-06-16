import dayjs from 'dayjs/esm';

export interface IAssetCategory {
  id: number;
  branchCode?: string | null;
  branchId?: string | null;
  assetCategoryCode?: string | null;
  assetCategoryName?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewAssetCategory = Omit<IAssetCategory, 'id'> & { id: null };
