import dayjs from 'dayjs/esm';
import { LiabilityType } from 'app/entities/enumerations/liability-type.model';
import { ApprovalStatus } from 'app/entities/enumerations/approval-status.model';

export interface ILiabilityLog {
  id: number;
  branchCode?: string | null;
  liabilityCode?: string | null;
  loanFrom?: string | null;
  description?: string | null;
  liabilityType?: keyof typeof LiabilityType | null;
  totalLoanAmount?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  interestPercentage?: number | null;
  monthlyPaymentAmount?: number | null;
  principalPaid?: number | null;
  balanceToPay?: number | null;
  status?: keyof typeof ApprovalStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewLiabilityLog = Omit<ILiabilityLog, 'id'> & { id: null };
