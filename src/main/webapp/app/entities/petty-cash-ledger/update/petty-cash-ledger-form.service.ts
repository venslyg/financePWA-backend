import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPettyCashLedger, NewPettyCashLedger } from '../petty-cash-ledger.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPettyCashLedger for edit and NewPettyCashLedgerFormGroupInput for create.
 */
type PettyCashLedgerFormGroupInput = IPettyCashLedger | PartialWithRequiredKeyOf<NewPettyCashLedger>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPettyCashLedger | NewPettyCashLedger> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type PettyCashLedgerFormRawValue = FormValueOf<IPettyCashLedger>;

type NewPettyCashLedgerFormRawValue = FormValueOf<NewPettyCashLedger>;

type PettyCashLedgerFormDefaults = Pick<NewPettyCashLedger, 'id' | 'createdDate' | 'lastModifiedDate'>;

type PettyCashLedgerFormGroupContent = {
  id: FormControl<PettyCashLedgerFormRawValue['id'] | NewPettyCashLedger['id']>;
  branchCode: FormControl<PettyCashLedgerFormRawValue['branchCode']>;
  branchId: FormControl<PettyCashLedgerFormRawValue['branchId']>;
  pettyCashCode: FormControl<PettyCashLedgerFormRawValue['pettyCashCode']>;
  date: FormControl<PettyCashLedgerFormRawValue['date']>;
  pettyCashVoucherNo: FormControl<PettyCashLedgerFormRawValue['pettyCashVoucherNo']>;
  description: FormControl<PettyCashLedgerFormRawValue['description']>;
  cashIn: FormControl<PettyCashLedgerFormRawValue['cashIn']>;
  cashOut: FormControl<PettyCashLedgerFormRawValue['cashOut']>;
  runningBalance: FormControl<PettyCashLedgerFormRawValue['runningBalance']>;
  linkedAccountCode: FormControl<PettyCashLedgerFormRawValue['linkedAccountCode']>;
  referenceNo: FormControl<PettyCashLedgerFormRawValue['referenceNo']>;
  createdBy: FormControl<PettyCashLedgerFormRawValue['createdBy']>;
  createdDate: FormControl<PettyCashLedgerFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<PettyCashLedgerFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<PettyCashLedgerFormRawValue['lastModifiedDate']>;
};

export type PettyCashLedgerFormGroup = FormGroup<PettyCashLedgerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PettyCashLedgerFormService {
  createPettyCashLedgerFormGroup(pettyCashLedger: PettyCashLedgerFormGroupInput = { id: null }): PettyCashLedgerFormGroup {
    const pettyCashLedgerRawValue = this.convertPettyCashLedgerToPettyCashLedgerRawValue({
      ...this.getFormDefaults(),
      ...pettyCashLedger,
    });
    return new FormGroup<PettyCashLedgerFormGroupContent>({
      id: new FormControl(
        { value: pettyCashLedgerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(pettyCashLedgerRawValue.branchCode),
      branchId: new FormControl(pettyCashLedgerRawValue.branchId),
      pettyCashCode: new FormControl(pettyCashLedgerRawValue.pettyCashCode),
      date: new FormControl(pettyCashLedgerRawValue.date),
      pettyCashVoucherNo: new FormControl(pettyCashLedgerRawValue.pettyCashVoucherNo),
      description: new FormControl(pettyCashLedgerRawValue.description),
      cashIn: new FormControl(pettyCashLedgerRawValue.cashIn),
      cashOut: new FormControl(pettyCashLedgerRawValue.cashOut),
      runningBalance: new FormControl(pettyCashLedgerRawValue.runningBalance),
      linkedAccountCode: new FormControl(pettyCashLedgerRawValue.linkedAccountCode),
      referenceNo: new FormControl(pettyCashLedgerRawValue.referenceNo),
      createdBy: new FormControl(pettyCashLedgerRawValue.createdBy),
      createdDate: new FormControl(pettyCashLedgerRawValue.createdDate),
      lastModifiedBy: new FormControl(pettyCashLedgerRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(pettyCashLedgerRawValue.lastModifiedDate),
    });
  }

  getPettyCashLedger(form: PettyCashLedgerFormGroup): IPettyCashLedger | NewPettyCashLedger {
    return this.convertPettyCashLedgerRawValueToPettyCashLedger(
      form.getRawValue() as PettyCashLedgerFormRawValue | NewPettyCashLedgerFormRawValue,
    );
  }

  resetForm(form: PettyCashLedgerFormGroup, pettyCashLedger: PettyCashLedgerFormGroupInput): void {
    const pettyCashLedgerRawValue = this.convertPettyCashLedgerToPettyCashLedgerRawValue({ ...this.getFormDefaults(), ...pettyCashLedger });
    form.reset(
      {
        ...pettyCashLedgerRawValue,
        id: { value: pettyCashLedgerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PettyCashLedgerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertPettyCashLedgerRawValueToPettyCashLedger(
    rawPettyCashLedger: PettyCashLedgerFormRawValue | NewPettyCashLedgerFormRawValue,
  ): IPettyCashLedger | NewPettyCashLedger {
    return {
      ...rawPettyCashLedger,
      createdDate: dayjs(rawPettyCashLedger.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawPettyCashLedger.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPettyCashLedgerToPettyCashLedgerRawValue(
    pettyCashLedger: IPettyCashLedger | (Partial<NewPettyCashLedger> & PettyCashLedgerFormDefaults),
  ): PettyCashLedgerFormRawValue | PartialWithRequiredKeyOf<NewPettyCashLedgerFormRawValue> {
    return {
      ...pettyCashLedger,
      createdDate: pettyCashLedger.createdDate ? pettyCashLedger.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: pettyCashLedger.lastModifiedDate ? pettyCashLedger.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
