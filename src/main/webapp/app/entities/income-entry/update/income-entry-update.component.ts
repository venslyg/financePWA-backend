import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IncomeType } from 'app/entities/enumerations/income-type.model';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { SyncStatus } from 'app/entities/enumerations/sync-status.model';
import { IncomeEntryService } from '../service/income-entry.service';
import { IIncomeEntry } from '../income-entry.model';
import { IncomeEntryFormGroup, IncomeEntryFormService } from './income-entry-form.service';

@Component({
  selector: 'jhi-income-entry-update',
  templateUrl: './income-entry-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IncomeEntryUpdateComponent implements OnInit {
  isSaving = false;
  incomeEntry: IIncomeEntry | null = null;
  incomeTypeValues = Object.keys(IncomeType);
  paymentModeValues = Object.keys(PaymentMode);
  syncStatusValues = Object.keys(SyncStatus);

  protected incomeEntryService = inject(IncomeEntryService);
  protected incomeEntryFormService = inject(IncomeEntryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IncomeEntryFormGroup = this.incomeEntryFormService.createIncomeEntryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeEntry }) => {
      this.incomeEntry = incomeEntry;
      if (incomeEntry) {
        this.updateForm(incomeEntry);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incomeEntry = this.incomeEntryFormService.getIncomeEntry(this.editForm);
    if (incomeEntry.id !== null) {
      this.subscribeToSaveResponse(this.incomeEntryService.update(incomeEntry));
    } else {
      this.subscribeToSaveResponse(this.incomeEntryService.create(incomeEntry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomeEntry>>): void {
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

  protected updateForm(incomeEntry: IIncomeEntry): void {
    this.incomeEntry = incomeEntry;
    this.incomeEntryFormService.resetForm(this.editForm, incomeEntry);
  }
}
