import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenseCategory, NewExpenseCategory } from '../expense-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseCategory for edit and NewExpenseCategoryFormGroupInput for create.
 */
type ExpenseCategoryFormGroupInput = IExpenseCategory | PartialWithRequiredKeyOf<NewExpenseCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpenseCategory | NewExpenseCategory> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ExpenseCategoryFormRawValue = FormValueOf<IExpenseCategory>;

type NewExpenseCategoryFormRawValue = FormValueOf<NewExpenseCategory>;

type ExpenseCategoryFormDefaults = Pick<NewExpenseCategory, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ExpenseCategoryFormGroupContent = {
  id: FormControl<ExpenseCategoryFormRawValue['id'] | NewExpenseCategory['id']>;
  branchCode: FormControl<ExpenseCategoryFormRawValue['branchCode']>;
  branchId: FormControl<ExpenseCategoryFormRawValue['branchId']>;
  categoryCode: FormControl<ExpenseCategoryFormRawValue['categoryCode']>;
  categoryName: FormControl<ExpenseCategoryFormRawValue['categoryName']>;
  description: FormControl<ExpenseCategoryFormRawValue['description']>;
  createdBy: FormControl<ExpenseCategoryFormRawValue['createdBy']>;
  createdDate: FormControl<ExpenseCategoryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ExpenseCategoryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ExpenseCategoryFormRawValue['lastModifiedDate']>;
};

export type ExpenseCategoryFormGroup = FormGroup<ExpenseCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseCategoryFormService {
  createExpenseCategoryFormGroup(expenseCategory: ExpenseCategoryFormGroupInput = { id: null }): ExpenseCategoryFormGroup {
    const expenseCategoryRawValue = this.convertExpenseCategoryToExpenseCategoryRawValue({
      ...this.getFormDefaults(),
      ...expenseCategory,
    });
    return new FormGroup<ExpenseCategoryFormGroupContent>({
      id: new FormControl(
        { value: expenseCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(expenseCategoryRawValue.branchCode),
      branchId: new FormControl(expenseCategoryRawValue.branchId),
      categoryCode: new FormControl(expenseCategoryRawValue.categoryCode),
      categoryName: new FormControl(expenseCategoryRawValue.categoryName),
      description: new FormControl(expenseCategoryRawValue.description),
      createdBy: new FormControl(expenseCategoryRawValue.createdBy),
      createdDate: new FormControl(expenseCategoryRawValue.createdDate),
      lastModifiedBy: new FormControl(expenseCategoryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(expenseCategoryRawValue.lastModifiedDate),
    });
  }

  getExpenseCategory(form: ExpenseCategoryFormGroup): IExpenseCategory | NewExpenseCategory {
    return this.convertExpenseCategoryRawValueToExpenseCategory(
      form.getRawValue() as ExpenseCategoryFormRawValue | NewExpenseCategoryFormRawValue,
    );
  }

  resetForm(form: ExpenseCategoryFormGroup, expenseCategory: ExpenseCategoryFormGroupInput): void {
    const expenseCategoryRawValue = this.convertExpenseCategoryToExpenseCategoryRawValue({ ...this.getFormDefaults(), ...expenseCategory });
    form.reset(
      {
        ...expenseCategoryRawValue,
        id: { value: expenseCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertExpenseCategoryRawValueToExpenseCategory(
    rawExpenseCategory: ExpenseCategoryFormRawValue | NewExpenseCategoryFormRawValue,
  ): IExpenseCategory | NewExpenseCategory {
    return {
      ...rawExpenseCategory,
      createdDate: dayjs(rawExpenseCategory.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawExpenseCategory.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseCategoryToExpenseCategoryRawValue(
    expenseCategory: IExpenseCategory | (Partial<NewExpenseCategory> & ExpenseCategoryFormDefaults),
  ): ExpenseCategoryFormRawValue | PartialWithRequiredKeyOf<NewExpenseCategoryFormRawValue> {
    return {
      ...expenseCategory,
      createdDate: expenseCategory.createdDate ? expenseCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: expenseCategory.lastModifiedDate ? expenseCategory.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
