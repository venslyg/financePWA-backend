import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { IDonationTracker } from '../donation-tracker.model';
import { DonationTrackerService } from '../service/donation-tracker.service';
import { DonationTrackerFormGroup, DonationTrackerFormService } from './donation-tracker-form.service';

@Component({
  selector: 'jhi-donation-tracker-update',
  templateUrl: './donation-tracker-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DonationTrackerUpdateComponent implements OnInit {
  isSaving = false;
  donationTracker: IDonationTracker | null = null;
  paymentModeValues = Object.keys(PaymentMode);

  protected donationTrackerService = inject(DonationTrackerService);
  protected donationTrackerFormService = inject(DonationTrackerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DonationTrackerFormGroup = this.donationTrackerFormService.createDonationTrackerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donationTracker }) => {
      this.donationTracker = donationTracker;
      if (donationTracker) {
        this.updateForm(donationTracker);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const donationTracker = this.donationTrackerFormService.getDonationTracker(this.editForm);
    if (donationTracker.id !== null) {
      this.subscribeToSaveResponse(this.donationTrackerService.update(donationTracker));
    } else {
      this.subscribeToSaveResponse(this.donationTrackerService.create(donationTracker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonationTracker>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(donationTracker: IDonationTracker): void {
    this.donationTracker = donationTracker;
    this.donationTrackerFormService.resetForm(this.editForm, donationTracker);
  }
}
