import dayjs from 'dayjs/esm';

export interface IBranch {
  id: number;
  branchCode?: string | null;
  branchName?: string | null;
  location?: string | null;
  phoneNumber?: string | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewBranch = Omit<IBranch, 'id'> & { id: null };
