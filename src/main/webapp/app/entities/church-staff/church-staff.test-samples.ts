import dayjs from 'dayjs/esm';

import { IChurchStaff, NewChurchStaff } from './church-staff.model';

export const sampleWithRequiredData: IChurchStaff = {
  id: 663,
};

export const sampleWithPartialData: IChurchStaff = {
  id: 16463,
  staffCode: 'following since',
  fullName: 'after',
  isActive: true,
  lastModifiedBy: 'ugh',
  lastModifiedDate: dayjs('2026-06-11T07:30'),
};

export const sampleWithFullData: IChurchStaff = {
  id: 5759,
  staffCode: 'ecliptic until',
  branchCode: 'wetly',
  fullName: 'unlike vaguely hence',
  position: 'with rebuke',
  staffType: 'CASUAL_WORKER',
  contactNumber: 'give gah brr',
  hourlyRateOrMonthlySalary: 477.89,
  isActive: false,
  createdBy: 'what scented utterly',
  createdDate: dayjs('2026-06-11T08:03'),
  lastModifiedBy: 'lasting alongside',
  lastModifiedDate: dayjs('2026-06-12T02:20'),
};

export const sampleWithNewData: NewChurchStaff = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
