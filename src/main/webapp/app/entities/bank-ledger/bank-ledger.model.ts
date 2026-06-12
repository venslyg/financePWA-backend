import dayjs from 'dayjs/esm';

export interface IBankLedger {
  id: number;
  branchCode?: string | null;
  bankLedgerCode?: string | null;
  date?: dayjs.Dayjs | null;
  referenceNo?: string | null;
  description?: string | null;
  depositAmount?: number | null;
  withdrawalAmount?: number | null;
  runningBalance?: number | null;
  remark?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewBankLedger = Omit<IBankLedger, 'id'> & { id: null };
