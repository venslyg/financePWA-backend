import dayjs from 'dayjs/esm';
import { IAssetRegister } from 'app/entities/asset-register/asset-register.model';
import { MaintenanceLogType } from 'app/entities/enumerations/maintenance-log-type.model';

export interface IMaintenanceLog {
  id: number;
  branchCode?: string | null;
  branchId?: string | null;
  maintenanceLogCode?: string | null;
  logDate?: dayjs.Dayjs | null;
  logType?: keyof typeof MaintenanceLogType | null;
  description?: string | null;
  cost?: number | null;
  vendor?: string | null;
  nextServiceDate?: dayjs.Dayjs | null;
  note?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  asset?: Pick<IAssetRegister, 'id' | 'assetRegisterCode'> | null;
}

export type NewMaintenanceLog = Omit<IMaintenanceLog, 'id'> & { id: null };
