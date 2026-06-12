import dayjs from 'dayjs/esm';

export interface IBinCardLine {
  id: number;
  inventoryItemCode?: string | null;
  date?: dayjs.Dayjs | null;
  referenceNo?: string | null;
  description?: string | null;
  quantityIn?: number | null;
  quantityOut?: number | null;
  runningBalance?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewBinCardLine = Omit<IBinCardLine, 'id'> & { id: null };
