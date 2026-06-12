import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';
import { AssetCategoryFormGroup, AssetCategoryFormService } from './asset-category-form.service';

@Component({
  selector: 'jhi-asset-category-update',
  templateUrl: './asset-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetCategoryUpdateComponent implements OnInit {
  isSaving = false;
  assetCategory: IAssetCategory | null = null;

  protected assetCategoryService = inject(AssetCategoryService);
  protected assetCategoryFormService = inject(AssetCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetCategoryFormGroup = this.assetCategoryFormService.createAssetCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetCategory }) => {
      this.assetCategory = assetCategory;
      if (assetCategory) {
        this.updateForm(assetCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetCategory = this.assetCategoryFormService.getAssetCategory(this.editForm);
    if (assetCategory.id !== null) {
      this.subscribeToSaveResponse(this.assetCategoryService.update(assetCategory));
    } else {
      this.subscribeToSaveResponse(this.assetCategoryService.create(assetCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetCategory>>): void {
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

  protected updateForm(assetCategory: IAssetCategory): void {
    this.assetCategory = assetCategory;
    this.assetCategoryFormService.resetForm(this.editForm, assetCategory);
  }
}
