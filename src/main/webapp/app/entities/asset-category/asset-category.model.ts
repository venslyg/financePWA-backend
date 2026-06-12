import dayjs from 'dayjs/esm';

export interface IAssetCategory {
  id: number;
  assetCategoryCode?: string | null;
  assetCategoryName?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewAssetCategory = Omit<IAssetCategory, 'id'> & { id: null };
