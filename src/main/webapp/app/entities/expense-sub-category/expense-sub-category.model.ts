import dayjs from 'dayjs/esm';
import { IExpenseCategory } from 'app/entities/expense-category/expense-category.model';

export interface IExpenseSubCategory {
  id: number;
  categoryCode?: string | null;
  subCategoryCode?: string | null;
  subCategoryName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  category?: Pick<IExpenseCategory, 'id' | 'categoryCode'> | null;
}

export type NewExpenseSubCategory = Omit<IExpenseSubCategory, 'id'> & { id: null };
