import dayjs from 'dayjs/esm';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { ApprovalStatus } from 'app/entities/enumerations/approval-status.model';
import { SyncStatus } from 'app/entities/enumerations/sync-status.model';

export interface IExpenseEntry {
  id: number;
  branchCode?: string | null;
  branchId?: string | null;
  accountCode?: string | null;
  expenseCode?: string | null;
  expenseCategoryCode?: string | null;
  expenseSubCategoryCode?: string | null;
  createdByUsername?: string | null;
  date?: dayjs.Dayjs | null;
  voucherNo?: string | null;
  description?: string | null;
  amount?: number | null;
  paymentMode?: keyof typeof PaymentMode | null;
  approvalStatus?: keyof typeof ApprovalStatus | null;
  approvedBy?: string | null;
  vendor?: string | null;
  syncStatus?: keyof typeof SyncStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewExpenseEntry = Omit<IExpenseEntry, 'id'> & { id: null };
