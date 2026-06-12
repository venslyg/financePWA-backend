import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetDepreciationHistory, NewAssetDepreciationHistory } from '../asset-depreciation-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetDepreciationHistory for edit and NewAssetDepreciationHistoryFormGroupInput for create.
 */
type AssetDepreciationHistoryFormGroupInput = IAssetDepreciationHistory | PartialWithRequiredKeyOf<NewAssetDepreciationHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssetDepreciationHistory | NewAssetDepreciationHistory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AssetDepreciationHistoryFormRawValue = FormValueOf<IAssetDepreciationHistory>;

type NewAssetDepreciationHistoryFormRawValue = FormValueOf<NewAssetDepreciationHistory>;

type AssetDepreciationHistoryFormDefaults = Pick<NewAssetDepreciationHistory, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AssetDepreciationHistoryFormGroupContent = {
  id: FormControl<AssetDepreciationHistoryFormRawValue['id'] | NewAssetDepreciationHistory['id']>;
  branchCode: FormControl<AssetDepreciationHistoryFormRawValue['branchCode']>;
  branchId: FormControl<AssetDepreciationHistoryFormRawValue['branchId']>;
  assetRegisterCode: FormControl<AssetDepreciationHistoryFormRawValue['assetRegisterCode']>;
  depreciationDate: FormControl<AssetDepreciationHistoryFormRawValue['depreciationDate']>;
  depreciationAmount: FormControl<AssetDepreciationHistoryFormRawValue['depreciationAmount']>;
  valueAfterDepreciation: FormControl<AssetDepreciationHistoryFormRawValue['valueAfterDepreciation']>;
  processedBy: FormControl<AssetDepreciationHistoryFormRawValue['processedBy']>;
  createdBy: FormControl<AssetDepreciationHistoryFormRawValue['createdBy']>;
  createdDate: FormControl<AssetDepreciationHistoryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AssetDepreciationHistoryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AssetDepreciationHistoryFormRawValue['lastModifiedDate']>;
};

export type AssetDepreciationHistoryFormGroup = FormGroup<AssetDepreciationHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetDepreciationHistoryFormService {
  createAssetDepreciationHistoryFormGroup(
    assetDepreciationHistory: AssetDepreciationHistoryFormGroupInput = { id: null },
  ): AssetDepreciationHistoryFormGroup {
    const assetDepreciationHistoryRawValue = this.convertAssetDepreciationHistoryToAssetDepreciationHistoryRawValue({
      ...this.getFormDefaults(),
      ...assetDepreciationHistory,
    });
    return new FormGroup<AssetDepreciationHistoryFormGroupContent>({
      id: new FormControl(
        { value: assetDepreciationHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(assetDepreciationHistoryRawValue.branchCode),
      branchId: new FormControl(assetDepreciationHistoryRawValue.branchId),
      assetRegisterCode: new FormControl(assetDepreciationHistoryRawValue.assetRegisterCode),
      depreciationDate: new FormControl(assetDepreciationHistoryRawValue.depreciationDate),
      depreciationAmount: new FormControl(assetDepreciationHistoryRawValue.depreciationAmount),
      valueAfterDepreciation: new FormControl(assetDepreciationHistoryRawValue.valueAfterDepreciation),
      processedBy: new FormControl(assetDepreciationHistoryRawValue.processedBy),
      createdBy: new FormControl(assetDepreciationHistoryRawValue.createdBy),
      createdDate: new FormControl(assetDepreciationHistoryRawValue.createdDate),
      lastModifiedBy: new FormControl(assetDepreciationHistoryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(assetDepreciationHistoryRawValue.lastModifiedDate),
    });
  }

  getAssetDepreciationHistory(form: AssetDepreciationHistoryFormGroup): IAssetDepreciationHistory | NewAssetDepreciationHistory {
    return this.convertAssetDepreciationHistoryRawValueToAssetDepreciationHistory(
      form.getRawValue() as AssetDepreciationHistoryFormRawValue | NewAssetDepreciationHistoryFormRawValue,
    );
  }

  resetForm(form: AssetDepreciationHistoryFormGroup, assetDepreciationHistory: AssetDepreciationHistoryFormGroupInput): void {
    const assetDepreciationHistoryRawValue = this.convertAssetDepreciationHistoryToAssetDepreciationHistoryRawValue({
      ...this.getFormDefaults(),
      ...assetDepreciationHistory,
    });
    form.reset(
      {
        ...assetDepreciationHistoryRawValue,
        id: { value: assetDepreciationHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetDepreciationHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAssetDepreciationHistoryRawValueToAssetDepreciationHistory(
    rawAssetDepreciationHistory: AssetDepreciationHistoryFormRawValue | NewAssetDepreciationHistoryFormRawValue,
  ): IAssetDepreciationHistory | NewAssetDepreciationHistory {
    return {
      ...rawAssetDepreciationHistory,
      createdDate: dayjs(rawAssetDepreciationHistory.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAssetDepreciationHistory.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAssetDepreciationHistoryToAssetDepreciationHistoryRawValue(
    assetDepreciationHistory: IAssetDepreciationHistory | (Partial<NewAssetDepreciationHistory> & AssetDepreciationHistoryFormDefaults),
  ): AssetDepreciationHistoryFormRawValue | PartialWithRequiredKeyOf<NewAssetDepreciationHistoryFormRawValue> {
    return {
      ...assetDepreciationHistory,
      createdDate: assetDepreciationHistory.createdDate ? assetDepreciationHistory.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: assetDepreciationHistory.lastModifiedDate
        ? assetDepreciationHistory.lastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
