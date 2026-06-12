import dayjs from 'dayjs/esm';

export interface IPettyCashLedger {
  id: number;
  branchCode?: string | null;
  pettyCashCode?: string | null;
  date?: dayjs.Dayjs | null;
  pettyCashVoucherNo?: string | null;
  description?: string | null;
  cashIn?: number | null;
  cashOut?: number | null;
  runningBalance?: number | null;
  linkedAccountCode?: string | null;
  referenceNo?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewPettyCashLedger = Omit<IPettyCashLedger, 'id'> & { id: null };
