import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBranch, NewBranch } from '../branch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBranch for edit and NewBranchFormGroupInput for create.
 */
type BranchFormGroupInput = IBranch | PartialWithRequiredKeyOf<NewBranch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBranch | NewBranch> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BranchFormRawValue = FormValueOf<IBranch>;

type NewBranchFormRawValue = FormValueOf<NewBranch>;

type BranchFormDefaults = Pick<NewBranch, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type BranchFormGroupContent = {
  id: FormControl<BranchFormRawValue['id'] | NewBranch['id']>;
  branchCode: FormControl<BranchFormRawValue['branchCode']>;
  branchName: FormControl<BranchFormRawValue['branchName']>;
  location: FormControl<BranchFormRawValue['location']>;
  phoneNumber: FormControl<BranchFormRawValue['phoneNumber']>;
  isActive: FormControl<BranchFormRawValue['isActive']>;
  createdBy: FormControl<BranchFormRawValue['createdBy']>;
  createdDate: FormControl<BranchFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BranchFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BranchFormRawValue['lastModifiedDate']>;
};

export type BranchFormGroup = FormGroup<BranchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BranchFormService {
  createBranchFormGroup(branch: BranchFormGroupInput = { id: null }): BranchFormGroup {
    const branchRawValue = this.convertBranchToBranchRawValue({
      ...this.getFormDefaults(),
      ...branch,
    });
    return new FormGroup<BranchFormGroupContent>({
      id: new FormControl(
        { value: branchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(branchRawValue.branchCode),
      branchName: new FormControl(branchRawValue.branchName),
      location: new FormControl(branchRawValue.location),
      phoneNumber: new FormControl(branchRawValue.phoneNumber),
      isActive: new FormControl(branchRawValue.isActive),
      createdBy: new FormControl(branchRawValue.createdBy),
      createdDate: new FormControl(branchRawValue.createdDate),
      lastModifiedBy: new FormControl(branchRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(branchRawValue.lastModifiedDate),
    });
  }

  getBranch(form: BranchFormGroup): IBranch | NewBranch {
    return this.convertBranchRawValueToBranch(form.getRawValue() as BranchFormRawValue | NewBranchFormRawValue);
  }

  resetForm(form: BranchFormGroup, branch: BranchFormGroupInput): void {
    const branchRawValue = this.convertBranchToBranchRawValue({ ...this.getFormDefaults(), ...branch });
    form.reset(
      {
        ...branchRawValue,
        id: { value: branchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BranchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBranchRawValueToBranch(rawBranch: BranchFormRawValue | NewBranchFormRawValue): IBranch | NewBranch {
    return {
      ...rawBranch,
      createdDate: dayjs(rawBranch.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBranch.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBranchToBranchRawValue(
    branch: IBranch | (Partial<NewBranch> & BranchFormDefaults),
  ): BranchFormRawValue | PartialWithRequiredKeyOf<NewBranchFormRawValue> {
    return {
      ...branch,
      createdDate: branch.createdDate ? branch.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: branch.lastModifiedDate ? branch.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
