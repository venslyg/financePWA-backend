import dayjs from 'dayjs/esm';
import { AccountType } from 'app/entities/enumerations/account-type.model';

export interface IAccountSet {
  id: number;
  branchCode?: string | null;
  accountCode?: string | null;
  accountName?: string | null;
  accountType?: keyof typeof AccountType | null;
  subCategory?: string | null;
  remark?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewAccountSet = Omit<IAccountSet, 'id'> & { id: null };
