import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBudgetPlan, NewBudgetPlan } from '../budget-plan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBudgetPlan for edit and NewBudgetPlanFormGroupInput for create.
 */
type BudgetPlanFormGroupInput = IBudgetPlan | PartialWithRequiredKeyOf<NewBudgetPlan>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBudgetPlan | NewBudgetPlan> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BudgetPlanFormRawValue = FormValueOf<IBudgetPlan>;

type NewBudgetPlanFormRawValue = FormValueOf<NewBudgetPlan>;

type BudgetPlanFormDefaults = Pick<NewBudgetPlan, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BudgetPlanFormGroupContent = {
  id: FormControl<BudgetPlanFormRawValue['id'] | NewBudgetPlan['id']>;
  branchCode: FormControl<BudgetPlanFormRawValue['branchCode']>;
  accountCode: FormControl<BudgetPlanFormRawValue['accountCode']>;
  budgetPlanCode: FormControl<BudgetPlanFormRawValue['budgetPlanCode']>;
  departmentName: FormControl<BudgetPlanFormRawValue['departmentName']>;
  year: FormControl<BudgetPlanFormRawValue['year']>;
  allocatedAmount: FormControl<BudgetPlanFormRawValue['allocatedAmount']>;
  spentAmount: FormControl<BudgetPlanFormRawValue['spentAmount']>;
  remainingAmount: FormControl<BudgetPlanFormRawValue['remainingAmount']>;
  usedPercentage: FormControl<BudgetPlanFormRawValue['usedPercentage']>;
  alertStatus: FormControl<BudgetPlanFormRawValue['alertStatus']>;
  createdBy: FormControl<BudgetPlanFormRawValue['createdBy']>;
  createdDate: FormControl<BudgetPlanFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BudgetPlanFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BudgetPlanFormRawValue['lastModifiedDate']>;
};

export type BudgetPlanFormGroup = FormGroup<BudgetPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BudgetPlanFormService {
  createBudgetPlanFormGroup(budgetPlan: BudgetPlanFormGroupInput = { id: null }): BudgetPlanFormGroup {
    const budgetPlanRawValue = this.convertBudgetPlanToBudgetPlanRawValue({
      ...this.getFormDefaults(),
      ...budgetPlan,
    });
    return new FormGroup<BudgetPlanFormGroupContent>({
      id: new FormControl(
        { value: budgetPlanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(budgetPlanRawValue.branchCode),
      accountCode: new FormControl(budgetPlanRawValue.accountCode),
      budgetPlanCode: new FormControl(budgetPlanRawValue.budgetPlanCode),
      departmentName: new FormControl(budgetPlanRawValue.departmentName),
      year: new FormControl(budgetPlanRawValue.year),
      allocatedAmount: new FormControl(budgetPlanRawValue.allocatedAmount),
      spentAmount: new FormControl(budgetPlanRawValue.spentAmount),
      remainingAmount: new FormControl(budgetPlanRawValue.remainingAmount),
      usedPercentage: new FormControl(budgetPlanRawValue.usedPercentage),
      alertStatus: new FormControl(budgetPlanRawValue.alertStatus),
      createdBy: new FormControl(budgetPlanRawValue.createdBy),
      createdDate: new FormControl(budgetPlanRawValue.createdDate),
      lastModifiedBy: new FormControl(budgetPlanRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(budgetPlanRawValue.lastModifiedDate),
    });
  }

  getBudgetPlan(form: BudgetPlanFormGroup): IBudgetPlan | NewBudgetPlan {
    return this.convertBudgetPlanRawValueToBudgetPlan(form.getRawValue() as BudgetPlanFormRawValue | NewBudgetPlanFormRawValue);
  }

  resetForm(form: BudgetPlanFormGroup, budgetPlan: BudgetPlanFormGroupInput): void {
    const budgetPlanRawValue = this.convertBudgetPlanToBudgetPlanRawValue({ ...this.getFormDefaults(), ...budgetPlan });
    form.reset(
      {
        ...budgetPlanRawValue,
        id: { value: budgetPlanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BudgetPlanFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBudgetPlanRawValueToBudgetPlan(
    rawBudgetPlan: BudgetPlanFormRawValue | NewBudgetPlanFormRawValue,
  ): IBudgetPlan | NewBudgetPlan {
    return {
      ...rawBudgetPlan,
      createdDate: dayjs(rawBudgetPlan.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBudgetPlan.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBudgetPlanToBudgetPlanRawValue(
    budgetPlan: IBudgetPlan | (Partial<NewBudgetPlan> & BudgetPlanFormDefaults),
  ): BudgetPlanFormRawValue | PartialWithRequiredKeyOf<NewBudgetPlanFormRawValue> {
    return {
      ...budgetPlan,
      createdDate: budgetPlan.createdDate ? budgetPlan.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: budgetPlan.lastModifiedDate ? budgetPlan.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
