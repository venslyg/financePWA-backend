import dayjs from 'dayjs/esm';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';

export interface IDonationTracker {
  id: number;
  branchCode?: string | null;
  donationIdCode?: string | null;
  date?: dayjs.Dayjs | null;
  donorNameOrOrg?: string | null;
  contactDetails?: string | null;
  amount?: number | null;
  purpose?: string | null;
  receivedViaMode?: keyof typeof PaymentMode | null;
  notes?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDonationTracker = Omit<IDonationTracker, 'id'> & { id: null };
