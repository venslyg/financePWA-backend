import dayjs from 'dayjs/esm';
import { IncomeType } from 'app/entities/enumerations/income-type.model';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { SyncStatus } from 'app/entities/enumerations/sync-status.model';

export interface IIncomeEntry {
  id: number;
  branchCode?: string | null;
  accountCode?: string | null;
  incomeCode?: string | null;
  createdByUsername?: string | null;
  date?: dayjs.Dayjs | null;
  receiptNo?: string | null;
  description?: string | null;
  incomeType?: keyof typeof IncomeType | null;
  amount?: number | null;
  paymentMethod?: keyof typeof PaymentMode | null;
  receivablePerson?: string | null;
  receivedBy?: string | null;
  syncStatus?: keyof typeof SyncStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewIncomeEntry = Omit<IIncomeEntry, 'id'> & { id: null };
