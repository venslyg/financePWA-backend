import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetSubCategory, NewAssetSubCategory } from '../asset-sub-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetSubCategory for edit and NewAssetSubCategoryFormGroupInput for create.
 */
type AssetSubCategoryFormGroupInput = IAssetSubCategory | PartialWithRequiredKeyOf<NewAssetSubCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssetSubCategory | NewAssetSubCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AssetSubCategoryFormRawValue = FormValueOf<IAssetSubCategory>;

type NewAssetSubCategoryFormRawValue = FormValueOf<NewAssetSubCategory>;

type AssetSubCategoryFormDefaults = Pick<NewAssetSubCategory, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AssetSubCategoryFormGroupContent = {
  id: FormControl<AssetSubCategoryFormRawValue['id'] | NewAssetSubCategory['id']>;
  branchCode: FormControl<AssetSubCategoryFormRawValue['branchCode']>;
  branchId: FormControl<AssetSubCategoryFormRawValue['branchId']>;
  assetCategoryCode: FormControl<AssetSubCategoryFormRawValue['assetCategoryCode']>;
  assetSubCategoryCode: FormControl<AssetSubCategoryFormRawValue['assetSubCategoryCode']>;
  assetSubCategoryName: FormControl<AssetSubCategoryFormRawValue['assetSubCategoryName']>;
  createdBy: FormControl<AssetSubCategoryFormRawValue['createdBy']>;
  createdDate: FormControl<AssetSubCategoryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AssetSubCategoryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AssetSubCategoryFormRawValue['lastModifiedDate']>;
  category: FormControl<AssetSubCategoryFormRawValue['category']>;
};

export type AssetSubCategoryFormGroup = FormGroup<AssetSubCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetSubCategoryFormService {
  createAssetSubCategoryFormGroup(assetSubCategory: AssetSubCategoryFormGroupInput = { id: null }): AssetSubCategoryFormGroup {
    const assetSubCategoryRawValue = this.convertAssetSubCategoryToAssetSubCategoryRawValue({
      ...this.getFormDefaults(),
      ...assetSubCategory,
    });
    return new FormGroup<AssetSubCategoryFormGroupContent>({
      id: new FormControl(
        { value: assetSubCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(assetSubCategoryRawValue.branchCode),
      branchId: new FormControl(assetSubCategoryRawValue.branchId),
      assetCategoryCode: new FormControl(assetSubCategoryRawValue.assetCategoryCode),
      assetSubCategoryCode: new FormControl(assetSubCategoryRawValue.assetSubCategoryCode),
      assetSubCategoryName: new FormControl(assetSubCategoryRawValue.assetSubCategoryName),
      createdBy: new FormControl(assetSubCategoryRawValue.createdBy),
      createdDate: new FormControl(assetSubCategoryRawValue.createdDate),
      lastModifiedBy: new FormControl(assetSubCategoryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(assetSubCategoryRawValue.lastModifiedDate),
      category: new FormControl(assetSubCategoryRawValue.category),
    });
  }

  getAssetSubCategory(form: AssetSubCategoryFormGroup): IAssetSubCategory | NewAssetSubCategory {
    return this.convertAssetSubCategoryRawValueToAssetSubCategory(
      form.getRawValue() as AssetSubCategoryFormRawValue | NewAssetSubCategoryFormRawValue,
    );
  }

  resetForm(form: AssetSubCategoryFormGroup, assetSubCategory: AssetSubCategoryFormGroupInput): void {
    const assetSubCategoryRawValue = this.convertAssetSubCategoryToAssetSubCategoryRawValue({
      ...this.getFormDefaults(),
      ...assetSubCategory,
    });
    form.reset(
      {
        ...assetSubCategoryRawValue,
        id: { value: assetSubCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetSubCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAssetSubCategoryRawValueToAssetSubCategory(
    rawAssetSubCategory: AssetSubCategoryFormRawValue | NewAssetSubCategoryFormRawValue,
  ): IAssetSubCategory | NewAssetSubCategory {
    return {
      ...rawAssetSubCategory,
      createdDate: dayjs(rawAssetSubCategory.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAssetSubCategory.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAssetSubCategoryToAssetSubCategoryRawValue(
    assetSubCategory: IAssetSubCategory | (Partial<NewAssetSubCategory> & AssetSubCategoryFormDefaults),
  ): AssetSubCategoryFormRawValue | PartialWithRequiredKeyOf<NewAssetSubCategoryFormRawValue> {
    return {
      ...assetSubCategory,
      createdDate: assetSubCategory.createdDate ? assetSubCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: assetSubCategory.lastModifiedDate ? assetSubCategory.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
