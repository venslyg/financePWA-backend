import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInventoryItem, NewInventoryItem } from '../inventory-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItem for edit and NewInventoryItemFormGroupInput for create.
 */
type InventoryItemFormGroupInput = IInventoryItem | PartialWithRequiredKeyOf<NewInventoryItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryItem | NewInventoryItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type InventoryItemFormRawValue = FormValueOf<IInventoryItem>;

type NewInventoryItemFormRawValue = FormValueOf<NewInventoryItem>;

type InventoryItemFormDefaults = Pick<NewInventoryItem, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type InventoryItemFormGroupContent = {
  id: FormControl<InventoryItemFormRawValue['id'] | NewInventoryItem['id']>;
  branchCode: FormControl<InventoryItemFormRawValue['branchCode']>;
  branchId: FormControl<InventoryItemFormRawValue['branchId']>;
  inventoryItemCode: FormControl<InventoryItemFormRawValue['inventoryItemCode']>;
  itemName: FormControl<InventoryItemFormRawValue['itemName']>;
  category: FormControl<InventoryItemFormRawValue['category']>;
  quantity: FormControl<InventoryItemFormRawValue['quantity']>;
  unitPrice: FormControl<InventoryItemFormRawValue['unitPrice']>;
  runningStockCount: FormControl<InventoryItemFormRawValue['runningStockCount']>;
  isActive: FormControl<InventoryItemFormRawValue['isActive']>;
  createdBy: FormControl<InventoryItemFormRawValue['createdBy']>;
  createdDate: FormControl<InventoryItemFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<InventoryItemFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<InventoryItemFormRawValue['lastModifiedDate']>;
};

export type InventoryItemFormGroup = FormGroup<InventoryItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InventoryItemFormService {
  createInventoryItemFormGroup(inventoryItem: InventoryItemFormGroupInput = { id: null }): InventoryItemFormGroup {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({
      ...this.getFormDefaults(),
      ...inventoryItem,
    });
    return new FormGroup<InventoryItemFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(inventoryItemRawValue.branchCode),
      branchId: new FormControl(inventoryItemRawValue.branchId),
      inventoryItemCode: new FormControl(inventoryItemRawValue.inventoryItemCode),
      itemName: new FormControl(inventoryItemRawValue.itemName),
      category: new FormControl(inventoryItemRawValue.category),
      quantity: new FormControl(inventoryItemRawValue.quantity),
      unitPrice: new FormControl(inventoryItemRawValue.unitPrice),
      runningStockCount: new FormControl(inventoryItemRawValue.runningStockCount),
      isActive: new FormControl(inventoryItemRawValue.isActive),
      createdBy: new FormControl(inventoryItemRawValue.createdBy),
      createdDate: new FormControl(inventoryItemRawValue.createdDate),
      lastModifiedBy: new FormControl(inventoryItemRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(inventoryItemRawValue.lastModifiedDate),
    });
  }

  getInventoryItem(form: InventoryItemFormGroup): IInventoryItem | NewInventoryItem {
    return this.convertInventoryItemRawValueToInventoryItem(form.getRawValue() as InventoryItemFormRawValue | NewInventoryItemFormRawValue);
  }

  resetForm(form: InventoryItemFormGroup, inventoryItem: InventoryItemFormGroupInput): void {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({ ...this.getFormDefaults(), ...inventoryItem });
    form.reset(
      {
        ...inventoryItemRawValue,
        id: { value: inventoryItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InventoryItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertInventoryItemRawValueToInventoryItem(
    rawInventoryItem: InventoryItemFormRawValue | NewInventoryItemFormRawValue,
  ): IInventoryItem | NewInventoryItem {
    return {
      ...rawInventoryItem,
      createdDate: dayjs(rawInventoryItem.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawInventoryItem.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryItemToInventoryItemRawValue(
    inventoryItem: IInventoryItem | (Partial<NewInventoryItem> & InventoryItemFormDefaults),
  ): InventoryItemFormRawValue | PartialWithRequiredKeyOf<NewInventoryItemFormRawValue> {
    return {
      ...inventoryItem,
      createdDate: inventoryItem.createdDate ? inventoryItem.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: inventoryItem.lastModifiedDate ? inventoryItem.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
