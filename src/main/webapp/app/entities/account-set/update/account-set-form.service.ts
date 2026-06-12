import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccountSet, NewAccountSet } from '../account-set.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountSet for edit and NewAccountSetFormGroupInput for create.
 */
type AccountSetFormGroupInput = IAccountSet | PartialWithRequiredKeyOf<NewAccountSet>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAccountSet | NewAccountSet> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AccountSetFormRawValue = FormValueOf<IAccountSet>;

type NewAccountSetFormRawValue = FormValueOf<NewAccountSet>;

type AccountSetFormDefaults = Pick<NewAccountSet, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AccountSetFormGroupContent = {
  id: FormControl<AccountSetFormRawValue['id'] | NewAccountSet['id']>;
  branchCode: FormControl<AccountSetFormRawValue['branchCode']>;
  branchId: FormControl<AccountSetFormRawValue['branchId']>;
  accountCode: FormControl<AccountSetFormRawValue['accountCode']>;
  accountName: FormControl<AccountSetFormRawValue['accountName']>;
  accountType: FormControl<AccountSetFormRawValue['accountType']>;
  subCategory: FormControl<AccountSetFormRawValue['subCategory']>;
  remark: FormControl<AccountSetFormRawValue['remark']>;
  createdBy: FormControl<AccountSetFormRawValue['createdBy']>;
  createdDate: FormControl<AccountSetFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AccountSetFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AccountSetFormRawValue['lastModifiedDate']>;
};

export type AccountSetFormGroup = FormGroup<AccountSetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountSetFormService {
  createAccountSetFormGroup(accountSet: AccountSetFormGroupInput = { id: null }): AccountSetFormGroup {
    const accountSetRawValue = this.convertAccountSetToAccountSetRawValue({
      ...this.getFormDefaults(),
      ...accountSet,
    });
    return new FormGroup<AccountSetFormGroupContent>({
      id: new FormControl(
        { value: accountSetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(accountSetRawValue.branchCode),
      branchId: new FormControl(accountSetRawValue.branchId),
      accountCode: new FormControl(accountSetRawValue.accountCode),
      accountName: new FormControl(accountSetRawValue.accountName),
      accountType: new FormControl(accountSetRawValue.accountType),
      subCategory: new FormControl(accountSetRawValue.subCategory),
      remark: new FormControl(accountSetRawValue.remark),
      createdBy: new FormControl(accountSetRawValue.createdBy),
      createdDate: new FormControl(accountSetRawValue.createdDate),
      lastModifiedBy: new FormControl(accountSetRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(accountSetRawValue.lastModifiedDate),
    });
  }

  getAccountSet(form: AccountSetFormGroup): IAccountSet | NewAccountSet {
    return this.convertAccountSetRawValueToAccountSet(form.getRawValue() as AccountSetFormRawValue | NewAccountSetFormRawValue);
  }

  resetForm(form: AccountSetFormGroup, accountSet: AccountSetFormGroupInput): void {
    const accountSetRawValue = this.convertAccountSetToAccountSetRawValue({ ...this.getFormDefaults(), ...accountSet });
    form.reset(
      {
        ...accountSetRawValue,
        id: { value: accountSetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountSetFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAccountSetRawValueToAccountSet(
    rawAccountSet: AccountSetFormRawValue | NewAccountSetFormRawValue,
  ): IAccountSet | NewAccountSet {
    return {
      ...rawAccountSet,
      createdDate: dayjs(rawAccountSet.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAccountSet.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAccountSetToAccountSetRawValue(
    accountSet: IAccountSet | (Partial<NewAccountSet> & AccountSetFormDefaults),
  ): AccountSetFormRawValue | PartialWithRequiredKeyOf<NewAccountSetFormRawValue> {
    return {
      ...accountSet,
      createdDate: accountSet.createdDate ? accountSet.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: accountSet.lastModifiedDate ? accountSet.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
