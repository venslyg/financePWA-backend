import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDonationTracker, NewDonationTracker } from '../donation-tracker.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDonationTracker for edit and NewDonationTrackerFormGroupInput for create.
 */
type DonationTrackerFormGroupInput = IDonationTracker | PartialWithRequiredKeyOf<NewDonationTracker>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDonationTracker | NewDonationTracker> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type DonationTrackerFormRawValue = FormValueOf<IDonationTracker>;

type NewDonationTrackerFormRawValue = FormValueOf<NewDonationTracker>;

type DonationTrackerFormDefaults = Pick<NewDonationTracker, 'id' | 'createdDate' | 'lastModifiedDate'>;

type DonationTrackerFormGroupContent = {
  id: FormControl<DonationTrackerFormRawValue['id'] | NewDonationTracker['id']>;
  branchCode: FormControl<DonationTrackerFormRawValue['branchCode']>;
  donationIdCode: FormControl<DonationTrackerFormRawValue['donationIdCode']>;
  date: FormControl<DonationTrackerFormRawValue['date']>;
  donorNameOrOrg: FormControl<DonationTrackerFormRawValue['donorNameOrOrg']>;
  contactDetails: FormControl<DonationTrackerFormRawValue['contactDetails']>;
  amount: FormControl<DonationTrackerFormRawValue['amount']>;
  purpose: FormControl<DonationTrackerFormRawValue['purpose']>;
  receivedViaMode: FormControl<DonationTrackerFormRawValue['receivedViaMode']>;
  notes: FormControl<DonationTrackerFormRawValue['notes']>;
  createdBy: FormControl<DonationTrackerFormRawValue['createdBy']>;
  createdDate: FormControl<DonationTrackerFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<DonationTrackerFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<DonationTrackerFormRawValue['lastModifiedDate']>;
};

export type DonationTrackerFormGroup = FormGroup<DonationTrackerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DonationTrackerFormService {
  createDonationTrackerFormGroup(donationTracker: DonationTrackerFormGroupInput = { id: null }): DonationTrackerFormGroup {
    const donationTrackerRawValue = this.convertDonationTrackerToDonationTrackerRawValue({
      ...this.getFormDefaults(),
      ...donationTracker,
    });
    return new FormGroup<DonationTrackerFormGroupContent>({
      id: new FormControl(
        { value: donationTrackerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      branchCode: new FormControl(donationTrackerRawValue.branchCode),
      donationIdCode: new FormControl(donationTrackerRawValue.donationIdCode),
      date: new FormControl(donationTrackerRawValue.date),
      donorNameOrOrg: new FormControl(donationTrackerRawValue.donorNameOrOrg),
      contactDetails: new FormControl(donationTrackerRawValue.contactDetails),
      amount: new FormControl(donationTrackerRawValue.amount),
      purpose: new FormControl(donationTrackerRawValue.purpose),
      receivedViaMode: new FormControl(donationTrackerRawValue.receivedViaMode),
      notes: new FormControl(donationTrackerRawValue.notes),
      createdBy: new FormControl(donationTrackerRawValue.createdBy),
      createdDate: new FormControl(donationTrackerRawValue.createdDate),
      lastModifiedBy: new FormControl(donationTrackerRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(donationTrackerRawValue.lastModifiedDate),
    });
  }

  getDonationTracker(form: DonationTrackerFormGroup): IDonationTracker | NewDonationTracker {
    return this.convertDonationTrackerRawValueToDonationTracker(
      form.getRawValue() as DonationTrackerFormRawValue | NewDonationTrackerFormRawValue,
    );
  }

  resetForm(form: DonationTrackerFormGroup, donationTracker: DonationTrackerFormGroupInput): void {
    const donationTrackerRawValue = this.convertDonationTrackerToDonationTrackerRawValue({ ...this.getFormDefaults(), ...donationTracker });
    form.reset(
      {
        ...donationTrackerRawValue,
        id: { value: donationTrackerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DonationTrackerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertDonationTrackerRawValueToDonationTracker(
    rawDonationTracker: DonationTrackerFormRawValue | NewDonationTrackerFormRawValue,
  ): IDonationTracker | NewDonationTracker {
    return {
      ...rawDonationTracker,
      createdDate: dayjs(rawDonationTracker.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawDonationTracker.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDonationTrackerToDonationTrackerRawValue(
    donationTracker: IDonationTracker | (Partial<NewDonationTracker> & DonationTrackerFormDefaults),
  ): DonationTrackerFormRawValue | PartialWithRequiredKeyOf<NewDonationTrackerFormRawValue> {
    return {
      ...donationTracker,
      createdDate: donationTracker.createdDate ? donationTracker.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: donationTracker.lastModifiedDate ? donationTracker.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
