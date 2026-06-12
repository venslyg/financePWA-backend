import dayjs from 'dayjs/esm';

import { IBinCardLine, NewBinCardLine } from './bin-card-line.model';

export const sampleWithRequiredData: IBinCardLine = {
  id: 15920,
};

export const sampleWithPartialData: IBinCardLine = {
  id: 31432,
  branchCode: 'energetically compromise',
  description: 'rubbery gadzooks',
  quantityIn: 5429.19,
  runningBalance: 28479.87,
  lastModifiedDate: dayjs('2026-06-11T16:02'),
};

export const sampleWithFullData: IBinCardLine = {
  id: 17386,
  branchCode: 'below uh-huh endow',
  branchId: 'whose excitedly',
  inventoryItemCode: 'exhausted',
  date: dayjs('2026-06-11'),
  referenceNo: 'intensely mentor',
  description: 'concentration bleakly up',
  quantityIn: 12449.79,
  quantityOut: 21409.69,
  runningBalance: 2161.95,
  createdBy: 'oval sans',
  createdDate: dayjs('2026-06-11T17:10'),
  lastModifiedBy: 'dreamily from hm',
  lastModifiedDate: dayjs('2026-06-12T03:36'),
};

export const sampleWithNewData: NewBinCardLine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
