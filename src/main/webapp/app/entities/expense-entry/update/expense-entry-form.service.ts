import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenseEntry, NewExpenseEntry } from '../expense-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseEntry for edit and NewExpenseEntryFormGroupInput for create.
 */
type ExpenseEntryFormGroupInput = IExpenseEntry | PartialWithRequiredKeyOf<NewExpenseEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpenseEntry | NewExpenseEntry> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ExpenseEntryFormRawValue = FormValueOf<IExpenseEntry>;

type NewExpenseEntryFormRawValue = FormValueOf<NewExpenseEntry>;

type ExpenseEntryFormDefaults = Pick<NewExpenseEntry, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ExpenseEntryFormGroupContent = {
  id: FormControl<ExpenseEntryFormRawValue['id'] | NewExpenseEntry['id']>;
  branchCode: FormControl<ExpenseEntryFormRawValue['branchCode']>;
  branchId: FormControl<ExpenseEntryFormRawValue['branchId']>;
  accountCode: FormControl<ExpenseEntryFormRawValue['accountCode']>;
  expenseCode: FormControl<ExpenseEntryFormRawValue['expenseCode']>;
  expenseCategoryCode: FormControl<ExpenseEntryFormRawValue['expenseCategoryCode']>;
  expenseSubCategoryCode: FormControl<ExpenseEntryFormRawValue['expenseSubCategoryCode']>;
  createdByUsername: FormControl<ExpenseEntryFormRawValue['createdByUsername']>;
  date: FormControl<ExpenseEntryFormRawValue['date']>;
  voucherNo: FormControl<ExpenseEntryFormRawValue['voucherNo']>;
  description: FormControl<ExpenseEntryFormRawValue['description']>;
  amount: FormControl<ExpenseEntryFormRawValue['amount']>;
  paymentMode: FormControl<ExpenseEntryFormRawValue['paymentMode']>;
  approvalStatus: FormControl<ExpenseEntryFormRawValue['approvalStatus']>;
  approvedBy: FormControl<ExpenseEntryFormRawValue['approvedBy']>;
  vendor: FormControl<ExpenseEntryFormRawValue['vendor']>;
  syncStatus: FormControl<ExpenseEntryFormRawValue['syncStatus']>;
  createdBy: FormControl<ExpenseEntryFormRawValue['createdBy']>;
  createdDate: FormControl<ExpenseEntryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ExpenseEntryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ExpenseEntryFormRawValue['lastModifiedDate']>;
};

export type ExpenseEntryFormGroup = FormGroup<ExpenseEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseEntryFormService {
  createExpenseEntryFormGroup(expenseEntry: ExpenseEntryFormGroupInput = { id: null }): ExpenseEntryFormGroup {
    const expenseEntryRawValue = this.convertExpenseEntryToExpenseEntryRawValue({
      ...this.getFormDefaults(),
      ...expenseEntry,
    });
    return new FormGroup<ExpenseEntryFormGroupContent>({
      id: new FormControl(
        { value: expenseEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(expenseEntryRawValue.branchCode),
      branchId: new FormControl(expenseEntryRawValue.branchId),
      accountCode: new FormControl(expenseEntryRawValue.accountCode),
      expenseCode: new FormControl(expenseEntryRawValue.expenseCode),
      expenseCategoryCode: new FormControl(expenseEntryRawValue.expenseCategoryCode),
      expenseSubCategoryCode: new FormControl(expenseEntryRawValue.expenseSubCategoryCode),
      createdByUsername: new FormControl(expenseEntryRawValue.createdByUsername),
      date: new FormControl(expenseEntryRawValue.date),
      voucherNo: new FormControl(expenseEntryRawValue.voucherNo),
      description: new FormControl(expenseEntryRawValue.description),
      amount: new FormControl(expenseEntryRawValue.amount),
      paymentMode: new FormControl(expenseEntryRawValue.paymentMode),
      approvalStatus: new FormControl(expenseEntryRawValue.approvalStatus),
      approvedBy: new FormControl(expenseEntryRawValue.approvedBy),
      vendor: new FormControl(expenseEntryRawValue.vendor),
      syncStatus: new FormControl(expenseEntryRawValue.syncStatus),
      createdBy: new FormControl(expenseEntryRawValue.createdBy),
      createdDate: new FormControl(expenseEntryRawValue.createdDate),
      lastModifiedBy: new FormControl(expenseEntryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(expenseEntryRawValue.lastModifiedDate),
    });
  }

  getExpenseEntry(form: ExpenseEntryFormGroup): IExpenseEntry | NewExpenseEntry {
    return this.convertExpenseEntryRawValueToExpenseEntry(form.getRawValue() as ExpenseEntryFormRawValue | NewExpenseEntryFormRawValue);
  }

  resetForm(form: ExpenseEntryFormGroup, expenseEntry: ExpenseEntryFormGroupInput): void {
    const expenseEntryRawValue = this.convertExpenseEntryToExpenseEntryRawValue({ ...this.getFormDefaults(), ...expenseEntry });
    form.reset(
      {
        ...expenseEntryRawValue,
        id: { value: expenseEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertExpenseEntryRawValueToExpenseEntry(
    rawExpenseEntry: ExpenseEntryFormRawValue | NewExpenseEntryFormRawValue,
  ): IExpenseEntry | NewExpenseEntry {
    return {
      ...rawExpenseEntry,
      createdDate: dayjs(rawExpenseEntry.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawExpenseEntry.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseEntryToExpenseEntryRawValue(
    expenseEntry: IExpenseEntry | (Partial<NewExpenseEntry> & ExpenseEntryFormDefaults),
  ): ExpenseEntryFormRawValue | PartialWithRequiredKeyOf<NewExpenseEntryFormRawValue> {
    return {
      ...expenseEntry,
      createdDate: expenseEntry.createdDate ? expenseEntry.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: expenseEntry.lastModifiedDate ? expenseEntry.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
