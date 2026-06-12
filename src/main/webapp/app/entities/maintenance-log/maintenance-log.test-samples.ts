import dayjs from 'dayjs/esm';

import { IMaintenanceLog, NewMaintenanceLog } from './maintenance-log.model';

export const sampleWithRequiredData: IMaintenanceLog = {
  id: 14936,
};

export const sampleWithPartialData: IMaintenanceLog = {
  id: 23373,
  branchId: 'which shred whistle',
  maintenanceLogCode: 'when frantically',
  logDate: dayjs('2026-06-11'),
  logType: 'REPAIR',
  createdDate: dayjs('2026-06-11T23:38'),
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
  createdBy: 'developmental atrium',
  createdDate: dayjs('2026-06-11T17:11'),
  lastModifiedBy: 'reprove',
  lastModifiedDate: dayjs('2026-06-11T10:51'),
};

export const sampleWithNewData: NewMaintenanceLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
