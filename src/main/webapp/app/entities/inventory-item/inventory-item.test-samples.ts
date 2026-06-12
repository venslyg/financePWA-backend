import dayjs from 'dayjs/esm';

import { IInventoryItem, NewInventoryItem } from './inventory-item.model';

export const sampleWithRequiredData: IInventoryItem = {
  id: 19345,
};

export const sampleWithPartialData: IInventoryItem = {
  id: 11366,
  branchCode: 'imagineer concerning powerfully',
  itemName: 'uh-huh once hidden',
  unitPrice: 10624.37,
  createdBy: 'oof obstruct once',
  lastModifiedBy: 'worth',
  lastModifiedDate: dayjs('2026-06-11T16:17'),
};

export const sampleWithFullData: IInventoryItem = {
  id: 20465,
  branchCode: 'till distant',
  branchId: 'wilted meh left',
  inventoryItemCode: 'insistent validity ugh',
  itemName: 'baritone woot dishearten',
  category: 'goose in',
  quantity: 3121.77,
  unitPrice: 1429.63,
  runningStockCount: 9169.12,
  createdBy: 'windy weep',
  createdDate: dayjs('2026-06-11T11:52'),
  lastModifiedBy: 'hmph contradict ethical',
  lastModifiedDate: dayjs('2026-06-11T21:43'),
};

export const sampleWithNewData: NewInventoryItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
