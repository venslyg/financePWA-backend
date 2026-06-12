import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBinCardLine } from '../bin-card-line.model';
import { BinCardLineService } from '../service/bin-card-line.service';
import { BinCardLineFormGroup, BinCardLineFormService } from './bin-card-line-form.service';

@Component({
  selector: 'jhi-bin-card-line-update',
  templateUrl: './bin-card-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BinCardLineUpdateComponent implements OnInit {
  isSaving = false;
  binCardLine: IBinCardLine | null = null;

  protected binCardLineService = inject(BinCardLineService);
  protected binCardLineFormService = inject(BinCardLineFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BinCardLineFormGroup = this.binCardLineFormService.createBinCardLineFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ binCardLine }) => {
      this.binCardLine = binCardLine;
      if (binCardLine) {
        this.updateForm(binCardLine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const binCardLine = this.binCardLineFormService.getBinCardLine(this.editForm);
    if (binCardLine.id !== null) {
      this.subscribeToSaveResponse(this.binCardLineService.update(binCardLine));
    } else {
      this.subscribeToSaveResponse(this.binCardLineService.create(binCardLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBinCardLine>>): void {
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

  protected updateForm(binCardLine: IBinCardLine): void {
    this.binCardLine = binCardLine;
    this.binCardLineFormService.resetForm(this.editForm, binCardLine);
  }
}
