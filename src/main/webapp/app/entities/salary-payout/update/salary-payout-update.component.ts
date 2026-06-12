import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISalaryPayout } from '../salary-payout.model';
import { SalaryPayoutService } from '../service/salary-payout.service';
import { SalaryPayoutFormGroup, SalaryPayoutFormService } from './salary-payout-form.service';

@Component({
  selector: 'jhi-salary-payout-update',
  templateUrl: './salary-payout-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SalaryPayoutUpdateComponent implements OnInit {
  isSaving = false;
  salaryPayout: ISalaryPayout | null = null;

  protected salaryPayoutService = inject(SalaryPayoutService);
  protected salaryPayoutFormService = inject(SalaryPayoutFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SalaryPayoutFormGroup = this.salaryPayoutFormService.createSalaryPayoutFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryPayout }) => {
      this.salaryPayout = salaryPayout;
      if (salaryPayout) {
        this.updateForm(salaryPayout);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryPayout = this.salaryPayoutFormService.getSalaryPayout(this.editForm);
    if (salaryPayout.id !== null) {
      this.subscribeToSaveResponse(this.salaryPayoutService.update(salaryPayout));
    } else {
      this.subscribeToSaveResponse(this.salaryPayoutService.create(salaryPayout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryPayout>>): void {
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

  protected updateForm(salaryPayout: ISalaryPayout): void {
    this.salaryPayout = salaryPayout;
    this.salaryPayoutFormService.resetForm(this.editForm, salaryPayout);
  }
}
