import dayjs from 'dayjs/esm';

import { IInventoryItem, NewInventoryItem } from './inventory-item.model';

export const sampleWithRequiredData: IInventoryItem = {
  id: 19345,
};

export const sampleWithPartialData: IInventoryItem = {
  id: 14278,
  branchCode: 'step-mother notwithstanding',
  category: 'powerfully which',
  runningStockCount: 26591.38,
  createdDate: dayjs('2026-06-11T19:13'),
  lastModifiedDate: dayjs('2026-06-12T02:32'),
};

export const sampleWithFullData: IInventoryItem = {
  id: 20465,
  branchCode: 'till distant',
  inventoryItemCode: 'wilted meh left',
  itemName: 'insistent validity ugh',
  category: 'baritone woot dishearten',
  quantity: 14631.62,
  unitPrice: 26789.4,
  runningStockCount: 17802.08,
  createdBy: 'instead',
  createdDate: dayjs('2026-06-12T01:18'),
  lastModifiedBy: 'to frozen fooey',
  lastModifiedDate: dayjs('2026-06-12T02:20'),
};

export const sampleWithNewData: NewInventoryItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
