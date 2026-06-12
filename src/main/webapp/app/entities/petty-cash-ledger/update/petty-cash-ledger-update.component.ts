import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPettyCashLedger } from '../petty-cash-ledger.model';
import { PettyCashLedgerService } from '../service/petty-cash-ledger.service';
import { PettyCashLedgerFormGroup, PettyCashLedgerFormService } from './petty-cash-ledger-form.service';

@Component({
  selector: 'jhi-petty-cash-ledger-update',
  templateUrl: './petty-cash-ledger-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PettyCashLedgerUpdateComponent implements OnInit {
  isSaving = false;
  pettyCashLedger: IPettyCashLedger | null = null;

  protected pettyCashLedgerService = inject(PettyCashLedgerService);
  protected pettyCashLedgerFormService = inject(PettyCashLedgerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PettyCashLedgerFormGroup = this.pettyCashLedgerFormService.createPettyCashLedgerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pettyCashLedger }) => {
      this.pettyCashLedger = pettyCashLedger;
      if (pettyCashLedger) {
        this.updateForm(pettyCashLedger);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pettyCashLedger = this.pettyCashLedgerFormService.getPettyCashLedger(this.editForm);
    if (pettyCashLedger.id !== null) {
      this.subscribeToSaveResponse(this.pettyCashLedgerService.update(pettyCashLedger));
    } else {
      this.subscribeToSaveResponse(this.pettyCashLedgerService.create(pettyCashLedger));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPettyCashLedger>>): void {
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

  protected updateForm(pettyCashLedger: IPettyCashLedger): void {
    this.pettyCashLedger = pettyCashLedger;
    this.pettyCashLedgerFormService.resetForm(this.editForm, pettyCashLedger);
  }
}
