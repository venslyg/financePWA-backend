import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';
import { AssetDepreciationHistoryService } from '../service/asset-depreciation-history.service';
import { AssetDepreciationHistoryFormGroup, AssetDepreciationHistoryFormService } from './asset-depreciation-history-form.service';

@Component({
  selector: 'jhi-asset-depreciation-history-update',
  templateUrl: './asset-depreciation-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetDepreciationHistoryUpdateComponent implements OnInit {
  isSaving = false;
  assetDepreciationHistory: IAssetDepreciationHistory | null = null;

  protected assetDepreciationHistoryService = inject(AssetDepreciationHistoryService);
  protected assetDepreciationHistoryFormService = inject(AssetDepreciationHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetDepreciationHistoryFormGroup = this.assetDepreciationHistoryFormService.createAssetDepreciationHistoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDepreciationHistory }) => {
      this.assetDepreciationHistory = assetDepreciationHistory;
      if (assetDepreciationHistory) {
        this.updateForm(assetDepreciationHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetDepreciationHistory = this.assetDepreciationHistoryFormService.getAssetDepreciationHistory(this.editForm);
    if (assetDepreciationHistory.id !== null) {
      this.subscribeToSaveResponse(this.assetDepreciationHistoryService.update(assetDepreciationHistory));
    } else {
      this.subscribeToSaveResponse(this.assetDepreciationHistoryService.create(assetDepreciationHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetDepreciationHistory>>): void {
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

  protected updateForm(assetDepreciationHistory: IAssetDepreciationHistory): void {
    this.assetDepreciationHistory = assetDepreciationHistory;
    this.assetDepreciationHistoryFormService.resetForm(this.editForm, assetDepreciationHistory);
  }
}
