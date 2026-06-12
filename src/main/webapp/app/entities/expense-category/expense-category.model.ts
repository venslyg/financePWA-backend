import dayjs from 'dayjs/esm';

export interface IExpenseCategory {
  id: number;
  categoryCode?: string | null;
  categoryName?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewExpenseCategory = Omit<IExpenseCategory, 'id'> & { id: null };
