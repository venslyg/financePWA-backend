import dayjs from 'dayjs/esm';

import { IChurchStaff, NewChurchStaff } from './church-staff.model';

export const sampleWithRequiredData: IChurchStaff = {
  id: 663,
};

export const sampleWithPartialData: IChurchStaff = {
  id: 15982,
  staffCode: 'reassuringly completion',
  branchId: 'beneath out if',
  hourlyRateOrMonthlySalary: 1408.76,
  createdDate: dayjs('2026-06-11T16:54'),
  lastModifiedBy: 'how till around',
};

export const sampleWithFullData: IChurchStaff = {
  id: 5759,
  staffCode: 'ecliptic until',
  branchCode: 'wetly',
  branchId: 'unlike vaguely hence',
  fullName: 'with rebuke',
  position: 'digitize interior',
  staffType: 'PART_TIME',
  contactNumber: 'abaft than',
  hourlyRateOrMonthlySalary: 1655.57,
  isActive: false,
  createdBy: 'bright oh',
  createdDate: dayjs('2026-06-11T18:24'),
  lastModifiedBy: 'as the for',
  lastModifiedDate: dayjs('2026-06-12T06:12'),
};

export const sampleWithNewData: NewChurchStaff = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
