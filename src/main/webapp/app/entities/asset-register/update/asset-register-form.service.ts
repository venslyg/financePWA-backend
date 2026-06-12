import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetRegister, NewAssetRegister } from '../asset-register.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetRegister for edit and NewAssetRegisterFormGroupInput for create.
 */
type AssetRegisterFormGroupInput = IAssetRegister | PartialWithRequiredKeyOf<NewAssetRegister>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssetRegister | NewAssetRegister> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AssetRegisterFormRawValue = FormValueOf<IAssetRegister>;

type NewAssetRegisterFormRawValue = FormValueOf<NewAssetRegister>;

type AssetRegisterFormDefaults = Pick<NewAssetRegister, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AssetRegisterFormGroupContent = {
  id: FormControl<AssetRegisterFormRawValue['id'] | NewAssetRegister['id']>;
  branchCode: FormControl<AssetRegisterFormRawValue['branchCode']>;
  branchId: FormControl<AssetRegisterFormRawValue['branchId']>;
  assetRegisterCode: FormControl<AssetRegisterFormRawValue['assetRegisterCode']>;
  assetCategoryCode: FormControl<AssetRegisterFormRawValue['assetCategoryCode']>;
  assetSubCategoryCode: FormControl<AssetRegisterFormRawValue['assetSubCategoryCode']>;
  assetName: FormControl<AssetRegisterFormRawValue['assetName']>;
  category: FormControl<AssetRegisterFormRawValue['category']>;
  purchaseDate: FormControl<AssetRegisterFormRawValue['purchaseDate']>;
  purchaseCost: FormControl<AssetRegisterFormRawValue['purchaseCost']>;
  currentValue: FormControl<AssetRegisterFormRawValue['currentValue']>;
  depreciationRate: FormControl<AssetRegisterFormRawValue['depreciationRate']>;
  accumulatedDepreciation: FormControl<AssetRegisterFormRawValue['accumulatedDepreciation']>;
  createdBy: FormControl<AssetRegisterFormRawValue['createdBy']>;
  createdDate: FormControl<AssetRegisterFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AssetRegisterFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AssetRegisterFormRawValue['lastModifiedDate']>;
};

export type AssetRegisterFormGroup = FormGroup<AssetRegisterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetRegisterFormService {
  createAssetRegisterFormGroup(assetRegister: AssetRegisterFormGroupInput = { id: null }): AssetRegisterFormGroup {
    const assetRegisterRawValue = this.convertAssetRegisterToAssetRegisterRawValue({
      ...this.getFormDefaults(),
      ...assetRegister,
    });
    return new FormGroup<AssetRegisterFormGroupContent>({
      id: new FormControl(
        { value: assetRegisterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(assetRegisterRawValue.branchCode),
      branchId: new FormControl(assetRegisterRawValue.branchId),
      assetRegisterCode: new FormControl(assetRegisterRawValue.assetRegisterCode),
      assetCategoryCode: new FormControl(assetRegisterRawValue.assetCategoryCode),
      assetSubCategoryCode: new FormControl(assetRegisterRawValue.assetSubCategoryCode),
      assetName: new FormControl(assetRegisterRawValue.assetName),
      category: new FormControl(assetRegisterRawValue.category),
      purchaseDate: new FormControl(assetRegisterRawValue.purchaseDate),
      purchaseCost: new FormControl(assetRegisterRawValue.purchaseCost),
      currentValue: new FormControl(assetRegisterRawValue.currentValue),
      depreciationRate: new FormControl(assetRegisterRawValue.depreciationRate),
      accumulatedDepreciation: new FormControl(assetRegisterRawValue.accumulatedDepreciation),
      createdBy: new FormControl(assetRegisterRawValue.createdBy),
      createdDate: new FormControl(assetRegisterRawValue.createdDate),
      lastModifiedBy: new FormControl(assetRegisterRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(assetRegisterRawValue.lastModifiedDate),
    });
  }

  getAssetRegister(form: AssetRegisterFormGroup): IAssetRegister | NewAssetRegister {
    return this.convertAssetRegisterRawValueToAssetRegister(form.getRawValue() as AssetRegisterFormRawValue | NewAssetRegisterFormRawValue);
  }

  resetForm(form: AssetRegisterFormGroup, assetRegister: AssetRegisterFormGroupInput): void {
    const assetRegisterRawValue = this.convertAssetRegisterToAssetRegisterRawValue({ ...this.getFormDefaults(), ...assetRegister });
    form.reset(
      {
        ...assetRegisterRawValue,
        id: { value: assetRegisterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetRegisterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAssetRegisterRawValueToAssetRegister(
    rawAssetRegister: AssetRegisterFormRawValue | NewAssetRegisterFormRawValue,
  ): IAssetRegister | NewAssetRegister {
    return {
      ...rawAssetRegister,
      createdDate: dayjs(rawAssetRegister.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAssetRegister.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAssetRegisterToAssetRegisterRawValue(
    assetRegister: IAssetRegister | (Partial<NewAssetRegister> & AssetRegisterFormDefaults),
  ): AssetRegisterFormRawValue | PartialWithRequiredKeyOf<NewAssetRegisterFormRawValue> {
    return {
      ...assetRegister,
      createdDate: assetRegister.createdDate ? assetRegister.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: assetRegister.lastModifiedDate ? assetRegister.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
