import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenseSubCategory, NewExpenseSubCategory } from '../expense-sub-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseSubCategory for edit and NewExpenseSubCategoryFormGroupInput for create.
 */
type ExpenseSubCategoryFormGroupInput = IExpenseSubCategory | PartialWithRequiredKeyOf<NewExpenseSubCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpenseSubCategory | NewExpenseSubCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ExpenseSubCategoryFormRawValue = FormValueOf<IExpenseSubCategory>;

type NewExpenseSubCategoryFormRawValue = FormValueOf<NewExpenseSubCategory>;

type ExpenseSubCategoryFormDefaults = Pick<NewExpenseSubCategory, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ExpenseSubCategoryFormGroupContent = {
  id: FormControl<ExpenseSubCategoryFormRawValue['id'] | NewExpenseSubCategory['id']>;
  branchCode: FormControl<ExpenseSubCategoryFormRawValue['branchCode']>;
  branchId: FormControl<ExpenseSubCategoryFormRawValue['branchId']>;
  categoryCode: FormControl<ExpenseSubCategoryFormRawValue['categoryCode']>;
  subCategoryCode: FormControl<ExpenseSubCategoryFormRawValue['subCategoryCode']>;
  subCategoryName: FormControl<ExpenseSubCategoryFormRawValue['subCategoryName']>;
  createdBy: FormControl<ExpenseSubCategoryFormRawValue['createdBy']>;
  createdDate: FormControl<ExpenseSubCategoryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ExpenseSubCategoryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ExpenseSubCategoryFormRawValue['lastModifiedDate']>;
  category: FormControl<ExpenseSubCategoryFormRawValue['category']>;
};

export type ExpenseSubCategoryFormGroup = FormGroup<ExpenseSubCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseSubCategoryFormService {
  createExpenseSubCategoryFormGroup(expenseSubCategory: ExpenseSubCategoryFormGroupInput = { id: null }): ExpenseSubCategoryFormGroup {
    const expenseSubCategoryRawValue = this.convertExpenseSubCategoryToExpenseSubCategoryRawValue({
      ...this.getFormDefaults(),
      ...expenseSubCategory,
    });
    return new FormGroup<ExpenseSubCategoryFormGroupContent>({
      id: new FormControl(
        { value: expenseSubCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(expenseSubCategoryRawValue.branchCode),
      branchId: new FormControl(expenseSubCategoryRawValue.branchId),
      categoryCode: new FormControl(expenseSubCategoryRawValue.categoryCode),
      subCategoryCode: new FormControl(expenseSubCategoryRawValue.subCategoryCode),
      subCategoryName: new FormControl(expenseSubCategoryRawValue.subCategoryName),
      createdBy: new FormControl(expenseSubCategoryRawValue.createdBy),
      createdDate: new FormControl(expenseSubCategoryRawValue.createdDate),
      lastModifiedBy: new FormControl(expenseSubCategoryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(expenseSubCategoryRawValue.lastModifiedDate),
      category: new FormControl(expenseSubCategoryRawValue.category),
    });
  }

  getExpenseSubCategory(form: ExpenseSubCategoryFormGroup): IExpenseSubCategory | NewExpenseSubCategory {
    return this.convertExpenseSubCategoryRawValueToExpenseSubCategory(
      form.getRawValue() as ExpenseSubCategoryFormRawValue | NewExpenseSubCategoryFormRawValue,
    );
  }

  resetForm(form: ExpenseSubCategoryFormGroup, expenseSubCategory: ExpenseSubCategoryFormGroupInput): void {
    const expenseSubCategoryRawValue = this.convertExpenseSubCategoryToExpenseSubCategoryRawValue({
      ...this.getFormDefaults(),
      ...expenseSubCategory,
    });
    form.reset(
      {
        ...expenseSubCategoryRawValue,
        id: { value: expenseSubCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseSubCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertExpenseSubCategoryRawValueToExpenseSubCategory(
    rawExpenseSubCategory: ExpenseSubCategoryFormRawValue | NewExpenseSubCategoryFormRawValue,
  ): IExpenseSubCategory | NewExpenseSubCategory {
    return {
      ...rawExpenseSubCategory,
      createdDate: dayjs(rawExpenseSubCategory.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawExpenseSubCategory.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseSubCategoryToExpenseSubCategoryRawValue(
    expenseSubCategory: IExpenseSubCategory | (Partial<NewExpenseSubCategory> & ExpenseSubCategoryFormDefaults),
  ): ExpenseSubCategoryFormRawValue | PartialWithRequiredKeyOf<NewExpenseSubCategoryFormRawValue> {
    return {
      ...expenseSubCategory,
      createdDate: expenseSubCategory.createdDate ? expenseSubCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: expenseSubCategory.lastModifiedDate ? expenseSubCategory.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
