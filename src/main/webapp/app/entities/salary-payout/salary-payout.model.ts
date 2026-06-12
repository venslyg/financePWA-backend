import dayjs from 'dayjs/esm';

export interface ISalaryPayout {
  id: number;
  branchCode?: string | null;
  salaryPayoutCode?: string | null;
  staffCode?: string | null;
  payPeriod?: string | null;
  baseSalary?: number | null;
  allowances?: number | null;
  deductions?: number | null;
  netPay?: number | null;
  payoutDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewSalaryPayout = Omit<ISalaryPayout, 'id'> & { id: null };
