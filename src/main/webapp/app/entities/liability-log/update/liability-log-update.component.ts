import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LiabilityType } from 'app/entities/enumerations/liability-type.model';
import { ApprovalStatus } from 'app/entities/enumerations/approval-status.model';
import { ILiabilityLog } from '../liability-log.model';
import { LiabilityLogService } from '../service/liability-log.service';
import { LiabilityLogFormGroup, LiabilityLogFormService } from './liability-log-form.service';

@Component({
  selector: 'jhi-liability-log-update',
  templateUrl: './liability-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LiabilityLogUpdateComponent implements OnInit {
  isSaving = false;
  liabilityLog: ILiabilityLog | null = null;
  liabilityTypeValues = Object.keys(LiabilityType);
  approvalStatusValues = Object.keys(ApprovalStatus);

  protected liabilityLogService = inject(LiabilityLogService);
  protected liabilityLogFormService = inject(LiabilityLogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LiabilityLogFormGroup = this.liabilityLogFormService.createLiabilityLogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liabilityLog }) => {
      this.liabilityLog = liabilityLog;
      if (liabilityLog) {
        this.updateForm(liabilityLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const liabilityLog = this.liabilityLogFormService.getLiabilityLog(this.editForm);
    if (liabilityLog.id !== null) {
      this.subscribeToSaveResponse(this.liabilityLogService.update(liabilityLog));
    } else {
      this.subscribeToSaveResponse(this.liabilityLogService.create(liabilityLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILiabilityLog>>): void {
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

  protected updateForm(liabilityLog: ILiabilityLog): void {
    this.liabilityLog = liabilityLog;
    this.liabilityLogFormService.resetForm(this.editForm, liabilityLog);
  }
}
