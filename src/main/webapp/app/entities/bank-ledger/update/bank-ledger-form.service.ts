import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBankLedger, NewBankLedger } from '../bank-ledger.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBankLedger for edit and NewBankLedgerFormGroupInput for create.
 */
type BankLedgerFormGroupInput = IBankLedger | PartialWithRequiredKeyOf<NewBankLedger>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBankLedger | NewBankLedger> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BankLedgerFormRawValue = FormValueOf<IBankLedger>;

type NewBankLedgerFormRawValue = FormValueOf<NewBankLedger>;

type BankLedgerFormDefaults = Pick<NewBankLedger, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BankLedgerFormGroupContent = {
  id: FormControl<BankLedgerFormRawValue['id'] | NewBankLedger['id']>;
  branchCode: FormControl<BankLedgerFormRawValue['branchCode']>;
  bankLedgerCode: FormControl<BankLedgerFormRawValue['bankLedgerCode']>;
  date: FormControl<BankLedgerFormRawValue['date']>;
  referenceNo: FormControl<BankLedgerFormRawValue['referenceNo']>;
  description: FormControl<BankLedgerFormRawValue['description']>;
  depositAmount: FormControl<BankLedgerFormRawValue['depositAmount']>;
  withdrawalAmount: FormControl<BankLedgerFormRawValue['withdrawalAmount']>;
  runningBalance: FormControl<BankLedgerFormRawValue['runningBalance']>;
  remark: FormControl<BankLedgerFormRawValue['remark']>;
  createdBy: FormControl<BankLedgerFormRawValue['createdBy']>;
  createdDate: FormControl<BankLedgerFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BankLedgerFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BankLedgerFormRawValue['lastModifiedDate']>;
};

export type BankLedgerFormGroup = FormGroup<BankLedgerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BankLedgerFormService {
  createBankLedgerFormGroup(bankLedger: BankLedgerFormGroupInput = { id: null }): BankLedgerFormGroup {
    const bankLedgerRawValue = this.convertBankLedgerToBankLedgerRawValue({
      ...this.getFormDefaults(),
      ...bankLedger,
    });
    return new FormGroup<BankLedgerFormGroupContent>({
      id: new FormControl(
        { value: bankLedgerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(bankLedgerRawValue.branchCode),
      bankLedgerCode: new FormControl(bankLedgerRawValue.bankLedgerCode),
      date: new FormControl(bankLedgerRawValue.date),
      referenceNo: new FormControl(bankLedgerRawValue.referenceNo),
      description: new FormControl(bankLedgerRawValue.description),
      depositAmount: new FormControl(bankLedgerRawValue.depositAmount),
      withdrawalAmount: new FormControl(bankLedgerRawValue.withdrawalAmount),
      runningBalance: new FormControl(bankLedgerRawValue.runningBalance),
      remark: new FormControl(bankLedgerRawValue.remark),
      createdBy: new FormControl(bankLedgerRawValue.createdBy),
      createdDate: new FormControl(bankLedgerRawValue.createdDate),
      lastModifiedBy: new FormControl(bankLedgerRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(bankLedgerRawValue.lastModifiedDate),
    });
  }

  getBankLedger(form: BankLedgerFormGroup): IBankLedger | NewBankLedger {
    return this.convertBankLedgerRawValueToBankLedger(form.getRawValue() as BankLedgerFormRawValue | NewBankLedgerFormRawValue);
  }

  resetForm(form: BankLedgerFormGroup, bankLedger: BankLedgerFormGroupInput): void {
    const bankLedgerRawValue = this.convertBankLedgerToBankLedgerRawValue({ ...this.getFormDefaults(), ...bankLedger });
    form.reset(
      {
        ...bankLedgerRawValue,
        id: { value: bankLedgerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BankLedgerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBankLedgerRawValueToBankLedger(
    rawBankLedger: BankLedgerFormRawValue | NewBankLedgerFormRawValue,
  ): IBankLedger | NewBankLedger {
    return {
      ...rawBankLedger,
      createdDate: dayjs(rawBankLedger.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBankLedger.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBankLedgerToBankLedgerRawValue(
    bankLedger: IBankLedger | (Partial<NewBankLedger> & BankLedgerFormDefaults),
  ): BankLedgerFormRawValue | PartialWithRequiredKeyOf<NewBankLedgerFormRawValue> {
    return {
      ...bankLedger,
      createdDate: bankLedger.createdDate ? bankLedger.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: bankLedger.lastModifiedDate ? bankLedger.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
