import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISalaryPayout, NewSalaryPayout } from '../salary-payout.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalaryPayout for edit and NewSalaryPayoutFormGroupInput for create.
 */
type SalaryPayoutFormGroupInput = ISalaryPayout | PartialWithRequiredKeyOf<NewSalaryPayout>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISalaryPayout | NewSalaryPayout> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type SalaryPayoutFormRawValue = FormValueOf<ISalaryPayout>;

type NewSalaryPayoutFormRawValue = FormValueOf<NewSalaryPayout>;

type SalaryPayoutFormDefaults = Pick<NewSalaryPayout, 'id' | 'createdDate' | 'lastModifiedDate'>;

type SalaryPayoutFormGroupContent = {
  id: FormControl<SalaryPayoutFormRawValue['id'] | NewSalaryPayout['id']>;
  branchCode: FormControl<SalaryPayoutFormRawValue['branchCode']>;
  branchId: FormControl<SalaryPayoutFormRawValue['branchId']>;
  salaryPayoutCode: FormControl<SalaryPayoutFormRawValue['salaryPayoutCode']>;
  staffCode: FormControl<SalaryPayoutFormRawValue['staffCode']>;
  payPeriod: FormControl<SalaryPayoutFormRawValue['payPeriod']>;
  baseSalary: FormControl<SalaryPayoutFormRawValue['baseSalary']>;
  allowances: FormControl<SalaryPayoutFormRawValue['allowances']>;
  deductions: FormControl<SalaryPayoutFormRawValue['deductions']>;
  netPay: FormControl<SalaryPayoutFormRawValue['netPay']>;
  payoutDate: FormControl<SalaryPayoutFormRawValue['payoutDate']>;
  createdBy: FormControl<SalaryPayoutFormRawValue['createdBy']>;
  createdDate: FormControl<SalaryPayoutFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<SalaryPayoutFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<SalaryPayoutFormRawValue['lastModifiedDate']>;
};

export type SalaryPayoutFormGroup = FormGroup<SalaryPayoutFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryPayoutFormService {
  createSalaryPayoutFormGroup(salaryPayout: SalaryPayoutFormGroupInput = { id: null }): SalaryPayoutFormGroup {
    const salaryPayoutRawValue = this.convertSalaryPayoutToSalaryPayoutRawValue({
      ...this.getFormDefaults(),
      ...salaryPayout,
    });
    return new FormGroup<SalaryPayoutFormGroupContent>({
      id: new FormControl(
        { value: salaryPayoutRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(salaryPayoutRawValue.branchCode),
      branchId: new FormControl(salaryPayoutRawValue.branchId),
      salaryPayoutCode: new FormControl(salaryPayoutRawValue.salaryPayoutCode),
      staffCode: new FormControl(salaryPayoutRawValue.staffCode),
      payPeriod: new FormControl(salaryPayoutRawValue.payPeriod),
      baseSalary: new FormControl(salaryPayoutRawValue.baseSalary),
      allowances: new FormControl(salaryPayoutRawValue.allowances),
      deductions: new FormControl(salaryPayoutRawValue.deductions),
      netPay: new FormControl(salaryPayoutRawValue.netPay),
      payoutDate: new FormControl(salaryPayoutRawValue.payoutDate),
      createdBy: new FormControl(salaryPayoutRawValue.createdBy),
      createdDate: new FormControl(salaryPayoutRawValue.createdDate),
      lastModifiedBy: new FormControl(salaryPayoutRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(salaryPayoutRawValue.lastModifiedDate),
    });
  }

  getSalaryPayout(form: SalaryPayoutFormGroup): ISalaryPayout | NewSalaryPayout {
    return this.convertSalaryPayoutRawValueToSalaryPayout(form.getRawValue() as SalaryPayoutFormRawValue | NewSalaryPayoutFormRawValue);
  }

  resetForm(form: SalaryPayoutFormGroup, salaryPayout: SalaryPayoutFormGroupInput): void {
    const salaryPayoutRawValue = this.convertSalaryPayoutToSalaryPayoutRawValue({ ...this.getFormDefaults(), ...salaryPayout });
    form.reset(
      {
        ...salaryPayoutRawValue,
        id: { value: salaryPayoutRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SalaryPayoutFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertSalaryPayoutRawValueToSalaryPayout(
    rawSalaryPayout: SalaryPayoutFormRawValue | NewSalaryPayoutFormRawValue,
  ): ISalaryPayout | NewSalaryPayout {
    return {
      ...rawSalaryPayout,
      createdDate: dayjs(rawSalaryPayout.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawSalaryPayout.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertSalaryPayoutToSalaryPayoutRawValue(
    salaryPayout: ISalaryPayout | (Partial<NewSalaryPayout> & SalaryPayoutFormDefaults),
  ): SalaryPayoutFormRawValue | PartialWithRequiredKeyOf<NewSalaryPayoutFormRawValue> {
    return {
      ...salaryPayout,
      createdDate: salaryPayout.createdDate ? salaryPayout.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: salaryPayout.lastModifiedDate ? salaryPayout.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
