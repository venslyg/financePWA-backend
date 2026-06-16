import dayjs from 'dayjs/esm';

import { IMaintenanceLog, NewMaintenanceLog } from './maintenance-log.model';

export const sampleWithRequiredData: IMaintenanceLog = {
  id: 14936,
};

export const sampleWithPartialData: IMaintenanceLog = {
  id: 25622,
  branchId: 'gosh croon failing',
  maintenanceLogCode: 'lest beside',
  logDate: dayjs('2026-06-11'),
  logType: 'REPLACEMENT',
  createdBy: 'abscond',
};

export const sampleWithFullData: IMaintenanceLog = {
  id: 6169,
  branchCode: 'hm coliseum fencing',
  branchId: 'redesign',
  maintenanceLogCode: 'handy about bashfully',
  logDate: dayjs('2026-06-11'),
  logType: 'REPAIR',
  description: 'igloo knavishly',
  cost: 7913.45,
  vendor: 'correctly underneath',
  nextServiceDate: dayjs('2026-06-11'),
  note: 'freckle',
  isActive: true,
  createdBy: 'when heroine',
  createdDate: dayjs('2026-06-11T07:19'),
  lastModifiedBy: 'cumbersome',
  lastModifiedDate: dayjs('2026-06-11T07:44'),
};

export const sampleWithNewData: NewMaintenanceLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
