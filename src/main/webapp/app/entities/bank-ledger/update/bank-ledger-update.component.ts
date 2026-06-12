import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBankLedger } from '../bank-ledger.model';
import { BankLedgerService } from '../service/bank-ledger.service';
import { BankLedgerFormGroup, BankLedgerFormService } from './bank-ledger-form.service';

@Component({
  selector: 'jhi-bank-ledger-update',
  templateUrl: './bank-ledger-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BankLedgerUpdateComponent implements OnInit {
  isSaving = false;
  bankLedger: IBankLedger | null = null;

  protected bankLedgerService = inject(BankLedgerService);
  protected bankLedgerFormService = inject(BankLedgerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankLedgerFormGroup = this.bankLedgerFormService.createBankLedgerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankLedger }) => {
      this.bankLedger = bankLedger;
      if (bankLedger) {
        this.updateForm(bankLedger);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankLedger = this.bankLedgerFormService.getBankLedger(this.editForm);
    if (bankLedger.id !== null) {
      this.subscribeToSaveResponse(this.bankLedgerService.update(bankLedger));
    } else {
      this.subscribeToSaveResponse(this.bankLedgerService.create(bankLedger));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankLedger>>): void {
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

  protected updateForm(bankLedger: IBankLedger): void {
    this.bankLedger = bankLedger;
    this.bankLedgerFormService.resetForm(this.editForm, bankLedger);
  }
}
