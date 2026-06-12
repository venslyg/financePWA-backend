import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChurchStaff, NewChurchStaff } from '../church-staff.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChurchStaff for edit and NewChurchStaffFormGroupInput for create.
 */
type ChurchStaffFormGroupInput = IChurchStaff | PartialWithRequiredKeyOf<NewChurchStaff>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IChurchStaff | NewChurchStaff> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ChurchStaffFormRawValue = FormValueOf<IChurchStaff>;

type NewChurchStaffFormRawValue = FormValueOf<NewChurchStaff>;

type ChurchStaffFormDefaults = Pick<NewChurchStaff, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type ChurchStaffFormGroupContent = {
  id: FormControl<ChurchStaffFormRawValue['id'] | NewChurchStaff['id']>;
  staffCode: FormControl<ChurchStaffFormRawValue['staffCode']>;
  branchCode: FormControl<ChurchStaffFormRawValue['branchCode']>;
  fullName: FormControl<ChurchStaffFormRawValue['fullName']>;
  position: FormControl<ChurchStaffFormRawValue['position']>;
  staffType: FormControl<ChurchStaffFormRawValue['staffType']>;
  contactNumber: FormControl<ChurchStaffFormRawValue['contactNumber']>;
  hourlyRateOrMonthlySalary: FormControl<ChurchStaffFormRawValue['hourlyRateOrMonthlySalary']>;
  isActive: FormControl<ChurchStaffFormRawValue['isActive']>;
  createdBy: FormControl<ChurchStaffFormRawValue['createdBy']>;
  createdDate: FormControl<ChurchStaffFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ChurchStaffFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ChurchStaffFormRawValue['lastModifiedDate']>;
};

export type ChurchStaffFormGroup = FormGroup<ChurchStaffFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChurchStaffFormService {
  createChurchStaffFormGroup(churchStaff: ChurchStaffFormGroupInput = { id: null }): ChurchStaffFormGroup {
    const churchStaffRawValue = this.convertChurchStaffToChurchStaffRawValue({
      ...this.getFormDefaults(),
      ...churchStaff,
    });
    return new FormGroup<ChurchStaffFormGroupContent>({
      id: new FormControl(
        { value: churchStaffRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      staffCode: new FormControl(churchStaffRawValue.staffCode),
      branchCode: new FormControl(churchStaffRawValue.branchCode),
      fullName: new FormControl(churchStaffRawValue.fullName),
      position: new FormControl(churchStaffRawValue.position),
      staffType: new FormControl(churchStaffRawValue.staffType),
      contactNumber: new FormControl(churchStaffRawValue.contactNumber),
      hourlyRateOrMonthlySalary: new FormControl(churchStaffRawValue.hourlyRateOrMonthlySalary),
      isActive: new FormControl(churchStaffRawValue.isActive),
      createdBy: new FormControl(churchStaffRawValue.createdBy),
      createdDate: new FormControl(churchStaffRawValue.createdDate),
      lastModifiedBy: new FormControl(churchStaffRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(churchStaffRawValue.lastModifiedDate),
    });
  }

  getChurchStaff(form: ChurchStaffFormGroup): IChurchStaff | NewChurchStaff {
    return this.convertChurchStaffRawValueToChurchStaff(form.getRawValue() as ChurchStaffFormRawValue | NewChurchStaffFormRawValue);
  }

  resetForm(form: ChurchStaffFormGroup, churchStaff: ChurchStaffFormGroupInput): void {
    const churchStaffRawValue = this.convertChurchStaffToChurchStaffRawValue({ ...this.getFormDefaults(), ...churchStaff });
    form.reset(
      {
        ...churchStaffRawValue,
        id: { value: churchStaffRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChurchStaffFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertChurchStaffRawValueToChurchStaff(
    rawChurchStaff: ChurchStaffFormRawValue | NewChurchStaffFormRawValue,
  ): IChurchStaff | NewChurchStaff {
    return {
      ...rawChurchStaff,
      createdDate: dayjs(rawChurchStaff.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawChurchStaff.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertChurchStaffToChurchStaffRawValue(
    churchStaff: IChurchStaff | (Partial<NewChurchStaff> & ChurchStaffFormDefaults),
  ): ChurchStaffFormRawValue | PartialWithRequiredKeyOf<NewChurchStaffFormRawValue> {
    return {
      ...churchStaff,
      createdDate: churchStaff.createdDate ? churchStaff.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: churchStaff.lastModifiedDate ? churchStaff.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
