import dayjs from 'dayjs/esm';

import { IBinCardLine, NewBinCardLine } from './bin-card-line.model';

export const sampleWithRequiredData: IBinCardLine = {
  id: 15920,
};

export const sampleWithPartialData: IBinCardLine = {
  id: 19764,
  inventoryItemCode: 'cute',
  quantityOut: 8611.25,
  runningBalance: 6299.68,
  createdDate: dayjs('2026-06-11T12:17'),
};

export const sampleWithFullData: IBinCardLine = {
  id: 17386,
  inventoryItemCode: 'below uh-huh endow',
  date: dayjs('2026-06-11'),
  referenceNo: 'yum after exhausted',
  description: 'below quit',
  quantityIn: 18573.94,
  quantityOut: 25662.74,
  runningBalance: 18423.4,
  createdBy: 'fray loudly',
  createdDate: dayjs('2026-06-11T16:21'),
  lastModifiedBy: 'incidentally oval sans',
  lastModifiedDate: dayjs('2026-06-11T17:10'),
};

export const sampleWithNewData: NewBinCardLine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
