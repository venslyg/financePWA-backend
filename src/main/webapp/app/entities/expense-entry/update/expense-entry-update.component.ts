import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { ApprovalStatus } from 'app/entities/enumerations/approval-status.model';
import { SyncStatus } from 'app/entities/enumerations/sync-status.model';
import { ExpenseEntryService } from '../service/expense-entry.service';
import { IExpenseEntry } from '../expense-entry.model';
import { ExpenseEntryFormGroup, ExpenseEntryFormService } from './expense-entry-form.service';

@Component({
  selector: 'jhi-expense-entry-update',
  templateUrl: './expense-entry-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseEntryUpdateComponent implements OnInit {
  isSaving = false;
  expenseEntry: IExpenseEntry | null = null;
  paymentModeValues = Object.keys(PaymentMode);
  approvalStatusValues = Object.keys(ApprovalStatus);
  syncStatusValues = Object.keys(SyncStatus);

  protected expenseEntryService = inject(ExpenseEntryService);
  protected expenseEntryFormService = inject(ExpenseEntryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseEntryFormGroup = this.expenseEntryFormService.createExpenseEntryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseEntry }) => {
      this.expenseEntry = expenseEntry;
      if (expenseEntry) {
        this.updateForm(expenseEntry);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseEntry = this.expenseEntryFormService.getExpenseEntry(this.editForm);
    if (expenseEntry.id !== null) {
      this.subscribeToSaveResponse(this.expenseEntryService.update(expenseEntry));
    } else {
      this.subscribeToSaveResponse(this.expenseEntryService.create(expenseEntry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseEntry>>): void {
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

  protected updateForm(expenseEntry: IExpenseEntry): void {
    this.expenseEntry = expenseEntry;
    this.expenseEntryFormService.resetForm(this.editForm, expenseEntry);
  }
}
