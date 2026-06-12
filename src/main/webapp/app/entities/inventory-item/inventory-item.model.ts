import dayjs from 'dayjs/esm';

export interface IInventoryItem {
  id: number;
  branchCode?: string | null;
  branchId?: string | null;
  inventoryItemCode?: string | null;
  itemName?: string | null;
  category?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  runningStockCount?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewInventoryItem = Omit<IInventoryItem, 'id'> & { id: null };
