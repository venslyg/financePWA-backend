import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetCategory, NewAssetCategory } from '../asset-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetCategory for edit and NewAssetCategoryFormGroupInput for create.
 */
type AssetCategoryFormGroupInput = IAssetCategory | PartialWithRequiredKeyOf<NewAssetCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssetCategory | NewAssetCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AssetCategoryFormRawValue = FormValueOf<IAssetCategory>;

type NewAssetCategoryFormRawValue = FormValueOf<NewAssetCategory>;

type AssetCategoryFormDefaults = Pick<NewAssetCategory, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AssetCategoryFormGroupContent = {
  id: FormControl<AssetCategoryFormRawValue['id'] | NewAssetCategory['id']>;
  assetCategoryCode: FormControl<AssetCategoryFormRawValue['assetCategoryCode']>;
  assetCategoryName: FormControl<AssetCategoryFormRawValue['assetCategoryName']>;
  description: FormControl<AssetCategoryFormRawValue['description']>;
  createdBy: FormControl<AssetCategoryFormRawValue['createdBy']>;
  createdDate: FormControl<AssetCategoryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AssetCategoryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AssetCategoryFormRawValue['lastModifiedDate']>;
};

export type AssetCategoryFormGroup = FormGroup<AssetCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetCategoryFormService {
  createAssetCategoryFormGroup(assetCategory: AssetCategoryFormGroupInput = { id: null }): AssetCategoryFormGroup {
    const assetCategoryRawValue = this.convertAssetCategoryToAssetCategoryRawValue({
      ...this.getFormDefaults(),
      ...assetCategory,
    });
    return new FormGroup<AssetCategoryFormGroupContent>({
      id: new FormControl(
        { value: assetCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      assetCategoryCode: new FormControl(assetCategoryRawValue.assetCategoryCode),
      assetCategoryName: new FormControl(assetCategoryRawValue.assetCategoryName),
      description: new FormControl(assetCategoryRawValue.description),
      createdBy: new FormControl(assetCategoryRawValue.createdBy),
      createdDate: new FormControl(assetCategoryRawValue.createdDate),
      lastModifiedBy: new FormControl(assetCategoryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(assetCategoryRawValue.lastModifiedDate),
    });
  }

  getAssetCategory(form: AssetCategoryFormGroup): IAssetCategory | NewAssetCategory {
    return this.convertAssetCategoryRawValueToAssetCategory(form.getRawValue() as AssetCategoryFormRawValue | NewAssetCategoryFormRawValue);
  }

  resetForm(form: AssetCategoryFormGroup, assetCategory: AssetCategoryFormGroupInput): void {
    const assetCategoryRawValue = this.convertAssetCategoryToAssetCategoryRawValue({ ...this.getFormDefaults(), ...assetCategory });
    form.reset(
      {
        ...assetCategoryRawValue,
        id: { value: assetCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAssetCategoryRawValueToAssetCategory(
    rawAssetCategory: AssetCategoryFormRawValue | NewAssetCategoryFormRawValue,
  ): IAssetCategory | NewAssetCategory {
    return {
      ...rawAssetCategory,
      createdDate: dayjs(rawAssetCategory.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAssetCategory.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAssetCategoryToAssetCategoryRawValue(
    assetCategory: IAssetCategory | (Partial<NewAssetCategory> & AssetCategoryFormDefaults),
  ): AssetCategoryFormRawValue | PartialWithRequiredKeyOf<NewAssetCategoryFormRawValue> {
    return {
      ...assetCategory,
      createdDate: assetCategory.createdDate ? assetCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: assetCategory.lastModifiedDate ? assetCategory.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
