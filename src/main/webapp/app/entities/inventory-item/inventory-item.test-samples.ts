import dayjs from 'dayjs/esm';

import { IInventoryItem, NewInventoryItem } from './inventory-item.model';

export const sampleWithRequiredData: IInventoryItem = {
  id: 19345,
};

export const sampleWithPartialData: IInventoryItem = {
  id: 25369,
  branchCode: 'psst kowtow testimonial',
  itemName: 'including aw',
  unitPrice: 10087.07,
  isActive: true,
  createdDate: dayjs('2026-06-12T04:54'),
  lastModifiedBy: 'hoick though hastily',
  lastModifiedDate: dayjs('2026-06-11T08:18'),
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
  isActive: false,
  createdBy: 'frozen fooey kiddingly',
  createdDate: dayjs('2026-06-12T03:26'),
  lastModifiedBy: 'far',
  lastModifiedDate: dayjs('2026-06-12T06:49'),
};

export const sampleWithNewData: NewInventoryItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
