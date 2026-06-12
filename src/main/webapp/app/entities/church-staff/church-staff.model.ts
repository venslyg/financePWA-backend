import dayjs from 'dayjs/esm';
import { StaffType } from 'app/entities/enumerations/staff-type.model';

export interface IChurchStaff {
  id: number;
  staffCode?: string | null;
  branchCode?: string | null;
  branchId?: string | null;
  fullName?: string | null;
  position?: string | null;
  staffType?: keyof typeof StaffType | null;
  contactNumber?: string | null;
  hourlyRateOrMonthlySalary?: number | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewChurchStaff = Omit<IChurchStaff, 'id'> & { id: null };
