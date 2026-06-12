import dayjs from 'dayjs/esm';
import { IAssetCategory } from 'app/entities/asset-category/asset-category.model';

export interface IAssetSubCategory {
  id: number;
  branchCode?: string | null;
  branchId?: string | null;
  assetCategoryCode?: string | null;
  assetSubCategoryCode?: string | null;
  assetSubCategoryName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  category?: Pick<IAssetCategory, 'id' | 'assetCategoryCode'> | null;
}

export type NewAssetSubCategory = Omit<IAssetSubCategory, 'id'> & { id: null };
