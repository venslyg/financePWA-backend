import dayjs from 'dayjs/esm';

import { IMaintenanceLog, NewMaintenanceLog } from './maintenance-log.model';

export const sampleWithRequiredData: IMaintenanceLog = {
  id: 14936,
};

export const sampleWithPartialData: IMaintenanceLog = {
  id: 21298,
  logDate: dayjs('2026-06-11'),
  logType: 'REPLACEMENT',
  description: 'which shred whistle',
  cost: 14711.31,
  lastModifiedDate: dayjs('2026-06-11T22:05'),
};

export const sampleWithFullData: IMaintenanceLog = {
  id: 6169,
  maintenanceLogCode: 'hm coliseum fencing',
  logDate: dayjs('2026-06-11'),
  logType: 'REPAIR',
  description: 'refine ham about',
  cost: 18237.73,
  vendor: 'information igloo knavishly',
  nextServiceDate: dayjs('2026-06-11'),
  note: 'correctly underneath',
  createdBy: 'duh intent',
  createdDate: dayjs('2026-06-11T20:47'),
  lastModifiedBy: 'atrium',
  lastModifiedDate: dayjs('2026-06-11T17:11'),
};

export const sampleWithNewData: NewMaintenanceLog = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
