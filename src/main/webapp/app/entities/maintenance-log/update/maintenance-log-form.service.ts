import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaintenanceLog, NewMaintenanceLog } from '../maintenance-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintenanceLog for edit and NewMaintenanceLogFormGroupInput for create.
 */
type MaintenanceLogFormGroupInput = IMaintenanceLog | PartialWithRequiredKeyOf<NewMaintenanceLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMaintenanceLog | NewMaintenanceLog> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type MaintenanceLogFormRawValue = FormValueOf<IMaintenanceLog>;

type NewMaintenanceLogFormRawValue = FormValueOf<NewMaintenanceLog>;

type MaintenanceLogFormDefaults = Pick<NewMaintenanceLog, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type MaintenanceLogFormGroupContent = {
  id: FormControl<MaintenanceLogFormRawValue['id'] | NewMaintenanceLog['id']>;
  branchCode: FormControl<MaintenanceLogFormRawValue['branchCode']>;
  branchId: FormControl<MaintenanceLogFormRawValue['branchId']>;
  maintenanceLogCode: FormControl<MaintenanceLogFormRawValue['maintenanceLogCode']>;
  logDate: FormControl<MaintenanceLogFormRawValue['logDate']>;
  logType: FormControl<MaintenanceLogFormRawValue['logType']>;
  description: FormControl<MaintenanceLogFormRawValue['description']>;
  cost: FormControl<MaintenanceLogFormRawValue['cost']>;
  vendor: FormControl<MaintenanceLogFormRawValue['vendor']>;
  nextServiceDate: FormControl<MaintenanceLogFormRawValue['nextServiceDate']>;
  note: FormControl<MaintenanceLogFormRawValue['note']>;
  isActive: FormControl<MaintenanceLogFormRawValue['isActive']>;
  createdBy: FormControl<MaintenanceLogFormRawValue['createdBy']>;
  createdDate: FormControl<MaintenanceLogFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<MaintenanceLogFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<MaintenanceLogFormRawValue['lastModifiedDate']>;
  asset: FormControl<MaintenanceLogFormRawValue['asset']>;
};

export type MaintenanceLogFormGroup = FormGroup<MaintenanceLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaintenanceLogFormService {
  createMaintenanceLogFormGroup(maintenanceLog: MaintenanceLogFormGroupInput = { id: null }): MaintenanceLogFormGroup {
    const maintenanceLogRawValue = this.convertMaintenanceLogToMaintenanceLogRawValue({
      ...this.getFormDefaults(),
      ...maintenanceLog,
    });
    return new FormGroup<MaintenanceLogFormGroupContent>({
      id: new FormControl(
        { value: maintenanceLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(maintenanceLogRawValue.branchCode),
      branchId: new FormControl(maintenanceLogRawValue.branchId),
      maintenanceLogCode: new FormControl(maintenanceLogRawValue.maintenanceLogCode),
      logDate: new FormControl(maintenanceLogRawValue.logDate),
      logType: new FormControl(maintenanceLogRawValue.logType),
      description: new FormControl(maintenanceLogRawValue.description),
      cost: new FormControl(maintenanceLogRawValue.cost),
      vendor: new FormControl(maintenanceLogRawValue.vendor),
      nextServiceDate: new FormControl(maintenanceLogRawValue.nextServiceDate),
      note: new FormControl(maintenanceLogRawValue.note),
      isActive: new FormControl(maintenanceLogRawValue.isActive),
      createdBy: new FormControl(maintenanceLogRawValue.createdBy),
      createdDate: new FormControl(maintenanceLogRawValue.createdDate),
      lastModifiedBy: new FormControl(maintenanceLogRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(maintenanceLogRawValue.lastModifiedDate),
      asset: new FormControl(maintenanceLogRawValue.asset),
    });
  }

  getMaintenanceLog(form: MaintenanceLogFormGroup): IMaintenanceLog | NewMaintenanceLog {
    return this.convertMaintenanceLogRawValueToMaintenanceLog(
      form.getRawValue() as MaintenanceLogFormRawValue | NewMaintenanceLogFormRawValue,
    );
  }

  resetForm(form: MaintenanceLogFormGroup, maintenanceLog: MaintenanceLogFormGroupInput): void {
    const maintenanceLogRawValue = this.convertMaintenanceLogToMaintenanceLogRawValue({ ...this.getFormDefaults(), ...maintenanceLog });
    form.reset(
      {
        ...maintenanceLogRawValue,
        id: { value: maintenanceLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaintenanceLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertMaintenanceLogRawValueToMaintenanceLog(
    rawMaintenanceLog: MaintenanceLogFormRawValue | NewMaintenanceLogFormRawValue,
  ): IMaintenanceLog | NewMaintenanceLog {
    return {
      ...rawMaintenanceLog,
      createdDate: dayjs(rawMaintenanceLog.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawMaintenanceLog.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMaintenanceLogToMaintenanceLogRawValue(
    maintenanceLog: IMaintenanceLog | (Partial<NewMaintenanceLog> & MaintenanceLogFormDefaults),
  ): MaintenanceLogFormRawValue | PartialWithRequiredKeyOf<NewMaintenanceLogFormRawValue> {
    return {
      ...maintenanceLog,
      createdDate: maintenanceLog.createdDate ? maintenanceLog.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: maintenanceLog.lastModifiedDate ? maintenanceLog.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
