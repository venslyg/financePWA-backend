import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILiabilityLog, NewLiabilityLog } from '../liability-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILiabilityLog for edit and NewLiabilityLogFormGroupInput for create.
 */
type LiabilityLogFormGroupInput = ILiabilityLog | PartialWithRequiredKeyOf<NewLiabilityLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILiabilityLog | NewLiabilityLog> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type LiabilityLogFormRawValue = FormValueOf<ILiabilityLog>;

type NewLiabilityLogFormRawValue = FormValueOf<NewLiabilityLog>;

type LiabilityLogFormDefaults = Pick<NewLiabilityLog, 'id' | 'createdDate' | 'lastModifiedDate'>;

type LiabilityLogFormGroupContent = {
  id: FormControl<LiabilityLogFormRawValue['id'] | NewLiabilityLog['id']>;
  branchCode: FormControl<LiabilityLogFormRawValue['branchCode']>;
  liabilityCode: FormControl<LiabilityLogFormRawValue['liabilityCode']>;
  loanFrom: FormControl<LiabilityLogFormRawValue['loanFrom']>;
  description: FormControl<LiabilityLogFormRawValue['description']>;
  liabilityType: FormControl<LiabilityLogFormRawValue['liabilityType']>;
  totalLoanAmount: FormControl<LiabilityLogFormRawValue['totalLoanAmount']>;
  startDate: FormControl<LiabilityLogFormRawValue['startDate']>;
  endDate: FormControl<LiabilityLogFormRawValue['endDate']>;
  interestPercentage: FormControl<LiabilityLogFormRawValue['interestPercentage']>;
  monthlyPaymentAmount: FormControl<LiabilityLogFormRawValue['monthlyPaymentAmount']>;
  principalPaid: FormControl<LiabilityLogFormRawValue['principalPaid']>;
  balanceToPay: FormControl<LiabilityLogFormRawValue['balanceToPay']>;
  status: FormControl<LiabilityLogFormRawValue['status']>;
  createdBy: FormControl<LiabilityLogFormRawValue['createdBy']>;
  createdDate: FormControl<LiabilityLogFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<LiabilityLogFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<LiabilityLogFormRawValue['lastModifiedDate']>;
};

export type LiabilityLogFormGroup = FormGroup<LiabilityLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LiabilityLogFormService {
  createLiabilityLogFormGroup(liabilityLog: LiabilityLogFormGroupInput = { id: null }): LiabilityLogFormGroup {
    const liabilityLogRawValue = this.convertLiabilityLogToLiabilityLogRawValue({
      ...this.getFormDefaults(),
      ...liabilityLog,
    });
    return new FormGroup<LiabilityLogFormGroupContent>({
      id: new FormControl(
        { value: liabilityLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(liabilityLogRawValue.branchCode),
      liabilityCode: new FormControl(liabilityLogRawValue.liabilityCode),
      loanFrom: new FormControl(liabilityLogRawValue.loanFrom),
      description: new FormControl(liabilityLogRawValue.description),
      liabilityType: new FormControl(liabilityLogRawValue.liabilityType),
      totalLoanAmount: new FormControl(liabilityLogRawValue.totalLoanAmount),
      startDate: new FormControl(liabilityLogRawValue.startDate),
      endDate: new FormControl(liabilityLogRawValue.endDate),
      interestPercentage: new FormControl(liabilityLogRawValue.interestPercentage),
      monthlyPaymentAmount: new FormControl(liabilityLogRawValue.monthlyPaymentAmount),
      principalPaid: new FormControl(liabilityLogRawValue.principalPaid),
      balanceToPay: new FormControl(liabilityLogRawValue.balanceToPay),
      status: new FormControl(liabilityLogRawValue.status),
      createdBy: new FormControl(liabilityLogRawValue.createdBy),
      createdDate: new FormControl(liabilityLogRawValue.createdDate),
      lastModifiedBy: new FormControl(liabilityLogRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(liabilityLogRawValue.lastModifiedDate),
    });
  }

  getLiabilityLog(form: LiabilityLogFormGroup): ILiabilityLog | NewLiabilityLog {
    return this.convertLiabilityLogRawValueToLiabilityLog(form.getRawValue() as LiabilityLogFormRawValue | NewLiabilityLogFormRawValue);
  }

  resetForm(form: LiabilityLogFormGroup, liabilityLog: LiabilityLogFormGroupInput): void {
    const liabilityLogRawValue = this.convertLiabilityLogToLiabilityLogRawValue({ ...this.getFormDefaults(), ...liabilityLog });
    form.reset(
      {
        ...liabilityLogRawValue,
        id: { value: liabilityLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LiabilityLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertLiabilityLogRawValueToLiabilityLog(
    rawLiabilityLog: LiabilityLogFormRawValue | NewLiabilityLogFormRawValue,
  ): ILiabilityLog | NewLiabilityLog {
    return {
      ...rawLiabilityLog,
      createdDate: dayjs(rawLiabilityLog.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawLiabilityLog.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertLiabilityLogToLiabilityLogRawValue(
    liabilityLog: ILiabilityLog | (Partial<NewLiabilityLog> & LiabilityLogFormDefaults),
  ): LiabilityLogFormRawValue | PartialWithRequiredKeyOf<NewLiabilityLogFormRawValue> {
    return {
      ...liabilityLog,
      createdDate: liabilityLog.createdDate ? liabilityLog.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: liabilityLog.lastModifiedDate ? liabilityLog.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
