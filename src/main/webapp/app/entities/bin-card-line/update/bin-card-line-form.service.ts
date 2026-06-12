import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBinCardLine, NewBinCardLine } from '../bin-card-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBinCardLine for edit and NewBinCardLineFormGroupInput for create.
 */
type BinCardLineFormGroupInput = IBinCardLine | PartialWithRequiredKeyOf<NewBinCardLine>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBinCardLine | NewBinCardLine> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BinCardLineFormRawValue = FormValueOf<IBinCardLine>;

type NewBinCardLineFormRawValue = FormValueOf<NewBinCardLine>;

type BinCardLineFormDefaults = Pick<NewBinCardLine, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BinCardLineFormGroupContent = {
  id: FormControl<BinCardLineFormRawValue['id'] | NewBinCardLine['id']>;
  branchCode: FormControl<BinCardLineFormRawValue['branchCode']>;
  branchId: FormControl<BinCardLineFormRawValue['branchId']>;
  inventoryItemCode: FormControl<BinCardLineFormRawValue['inventoryItemCode']>;
  date: FormControl<BinCardLineFormRawValue['date']>;
  referenceNo: FormControl<BinCardLineFormRawValue['referenceNo']>;
  description: FormControl<BinCardLineFormRawValue['description']>;
  quantityIn: FormControl<BinCardLineFormRawValue['quantityIn']>;
  quantityOut: FormControl<BinCardLineFormRawValue['quantityOut']>;
  runningBalance: FormControl<BinCardLineFormRawValue['runningBalance']>;
  createdBy: FormControl<BinCardLineFormRawValue['createdBy']>;
  createdDate: FormControl<BinCardLineFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BinCardLineFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BinCardLineFormRawValue['lastModifiedDate']>;
};

export type BinCardLineFormGroup = FormGroup<BinCardLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BinCardLineFormService {
  createBinCardLineFormGroup(binCardLine: BinCardLineFormGroupInput = { id: null }): BinCardLineFormGroup {
    const binCardLineRawValue = this.convertBinCardLineToBinCardLineRawValue({
      ...this.getFormDefaults(),
      ...binCardLine,
    });
    return new FormGroup<BinCardLineFormGroupContent>({
      id: new FormControl(
        { value: binCardLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(binCardLineRawValue.branchCode),
      branchId: new FormControl(binCardLineRawValue.branchId),
      inventoryItemCode: new FormControl(binCardLineRawValue.inventoryItemCode),
      date: new FormControl(binCardLineRawValue.date),
      referenceNo: new FormControl(binCardLineRawValue.referenceNo),
      description: new FormControl(binCardLineRawValue.description),
      quantityIn: new FormControl(binCardLineRawValue.quantityIn),
      quantityOut: new FormControl(binCardLineRawValue.quantityOut),
      runningBalance: new FormControl(binCardLineRawValue.runningBalance),
      createdBy: new FormControl(binCardLineRawValue.createdBy),
      createdDate: new FormControl(binCardLineRawValue.createdDate),
      lastModifiedBy: new FormControl(binCardLineRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(binCardLineRawValue.lastModifiedDate),
    });
  }

  getBinCardLine(form: BinCardLineFormGroup): IBinCardLine | NewBinCardLine {
    return this.convertBinCardLineRawValueToBinCardLine(form.getRawValue() as BinCardLineFormRawValue | NewBinCardLineFormRawValue);
  }

  resetForm(form: BinCardLineFormGroup, binCardLine: BinCardLineFormGroupInput): void {
    const binCardLineRawValue = this.convertBinCardLineToBinCardLineRawValue({ ...this.getFormDefaults(), ...binCardLine });
    form.reset(
      {
        ...binCardLineRawValue,
        id: { value: binCardLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BinCardLineFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBinCardLineRawValueToBinCardLine(
    rawBinCardLine: BinCardLineFormRawValue | NewBinCardLineFormRawValue,
  ): IBinCardLine | NewBinCardLine {
    return {
      ...rawBinCardLine,
      createdDate: dayjs(rawBinCardLine.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBinCardLine.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBinCardLineToBinCardLineRawValue(
    binCardLine: IBinCardLine | (Partial<NewBinCardLine> & BinCardLineFormDefaults),
  ): BinCardLineFormRawValue | PartialWithRequiredKeyOf<NewBinCardLineFormRawValue> {
    return {
      ...binCardLine,
      createdDate: binCardLine.createdDate ? binCardLine.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: binCardLine.lastModifiedDate ? binCardLine.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
