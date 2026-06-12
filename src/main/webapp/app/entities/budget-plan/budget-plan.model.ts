import dayjs from 'dayjs/esm';
import { BudgetAlertStatus } from 'app/entities/enumerations/budget-alert-status.model';

export interface IBudgetPlan {
  id: number;
  branchCode?: string | null;
  accountCode?: string | null;
  budgetPlanCode?: string | null;
  departmentName?: string | null;
  year?: number | null;
  allocatedAmount?: number | null;
  spentAmount?: number | null;
  remainingAmount?: number | null;
  usedPercentage?: number | null;
  alertStatus?: keyof typeof BudgetAlertStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewBudgetPlan = Omit<IBudgetPlan, 'id'> & { id: null };
