import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIncomeEntry, NewIncomeEntry } from '../income-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncomeEntry for edit and NewIncomeEntryFormGroupInput for create.
 */
type IncomeEntryFormGroupInput = IIncomeEntry | PartialWithRequiredKeyOf<NewIncomeEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIncomeEntry | NewIncomeEntry> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type IncomeEntryFormRawValue = FormValueOf<IIncomeEntry>;

type NewIncomeEntryFormRawValue = FormValueOf<NewIncomeEntry>;

type IncomeEntryFormDefaults = Pick<NewIncomeEntry, 'id' | 'createdDate' | 'lastModifiedDate'>;

type IncomeEntryFormGroupContent = {
  id: FormControl<IncomeEntryFormRawValue['id'] | NewIncomeEntry['id']>;
  branchCode: FormControl<IncomeEntryFormRawValue['branchCode']>;
  branchId: FormControl<IncomeEntryFormRawValue['branchId']>;
  accountCode: FormControl<IncomeEntryFormRawValue['accountCode']>;
  incomeCode: FormControl<IncomeEntryFormRawValue['incomeCode']>;
  createdByUsername: FormControl<IncomeEntryFormRawValue['createdByUsername']>;
  date: FormControl<IncomeEntryFormRawValue['date']>;
  receiptNo: FormControl<IncomeEntryFormRawValue['receiptNo']>;
  description: FormControl<IncomeEntryFormRawValue['description']>;
  incomeType: FormControl<IncomeEntryFormRawValue['incomeType']>;
  amount: FormControl<IncomeEntryFormRawValue['amount']>;
  paymentMethod: FormControl<IncomeEntryFormRawValue['paymentMethod']>;
  receivablePerson: FormControl<IncomeEntryFormRawValue['receivablePerson']>;
  receivedBy: FormControl<IncomeEntryFormRawValue['receivedBy']>;
  syncStatus: FormControl<IncomeEntryFormRawValue['syncStatus']>;
  createdBy: FormControl<IncomeEntryFormRawValue['createdBy']>;
  createdDate: FormControl<IncomeEntryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<IncomeEntryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<IncomeEntryFormRawValue['lastModifiedDate']>;
};

export type IncomeEntryFormGroup = FormGroup<IncomeEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncomeEntryFormService {
  createIncomeEntryFormGroup(incomeEntry: IncomeEntryFormGroupInput = { id: null }): IncomeEntryFormGroup {
    const incomeEntryRawValue = this.convertIncomeEntryToIncomeEntryRawValue({
      ...this.getFormDefaults(),
      ...incomeEntry,
    });
    return new FormGroup<IncomeEntryFormGroupContent>({
      id: new FormControl(
        { value: incomeEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(incomeEntryRawValue.branchCode),
      branchId: new FormControl(incomeEntryRawValue.branchId),
      accountCode: new FormControl(incomeEntryRawValue.accountCode),
      incomeCode: new FormControl(incomeEntryRawValue.incomeCode),
      createdByUsername: new FormControl(incomeEntryRawValue.createdByUsername),
      date: new FormControl(incomeEntryRawValue.date),
      receiptNo: new FormControl(incomeEntryRawValue.receiptNo),
      description: new FormControl(incomeEntryRawValue.description),
      incomeType: new FormControl(incomeEntryRawValue.incomeType),
      amount: new FormControl(incomeEntryRawValue.amount),
      paymentMethod: new FormControl(incomeEntryRawValue.paymentMethod),
      receivablePerson: new FormControl(incomeEntryRawValue.receivablePerson),
      receivedBy: new FormControl(incomeEntryRawValue.receivedBy),
      syncStatus: new FormControl(incomeEntryRawValue.syncStatus),
      createdBy: new FormControl(incomeEntryRawValue.createdBy),
      createdDate: new FormControl(incomeEntryRawValue.createdDate),
      lastModifiedBy: new FormControl(incomeEntryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(incomeEntryRawValue.lastModifiedDate),
    });
  }

  getIncomeEntry(form: IncomeEntryFormGroup): IIncomeEntry | NewIncomeEntry {
    return this.convertIncomeEntryRawValueToIncomeEntry(form.getRawValue() as IncomeEntryFormRawValue | NewIncomeEntryFormRawValue);
  }

  resetForm(form: IncomeEntryFormGroup, incomeEntry: IncomeEntryFormGroupInput): void {
    const incomeEntryRawValue = this.convertIncomeEntryToIncomeEntryRawValue({ ...this.getFormDefaults(), ...incomeEntry });
    form.reset(
      {
        ...incomeEntryRawValue,
        id: { value: incomeEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IncomeEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertIncomeEntryRawValueToIncomeEntry(
    rawIncomeEntry: IncomeEntryFormRawValue | NewIncomeEntryFormRawValue,
  ): IIncomeEntry | NewIncomeEntry {
    return {
      ...rawIncomeEntry,
      createdDate: dayjs(rawIncomeEntry.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawIncomeEntry.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertIncomeEntryToIncomeEntryRawValue(
    incomeEntry: IIncomeEntry | (Partial<NewIncomeEntry> & IncomeEntryFormDefaults),
  ): IncomeEntryFormRawValue | PartialWithRequiredKeyOf<NewIncomeEntryFormRawValue> {
    return {
      ...incomeEntry,
      createdDate: incomeEntry.createdDate ? incomeEntry.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: incomeEntry.lastModifiedDate ? incomeEntry.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
