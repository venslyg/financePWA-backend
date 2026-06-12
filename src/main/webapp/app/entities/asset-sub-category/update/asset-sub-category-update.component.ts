import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetCategory } from 'app/entities/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/asset-category/service/asset-category.service';
import { IAssetSubCategory } from '../asset-sub-category.model';
import { AssetSubCategoryService } from '../service/asset-sub-category.service';
import { AssetSubCategoryFormGroup, AssetSubCategoryFormService } from './asset-sub-category-form.service';

@Component({
  selector: 'jhi-asset-sub-category-update',
  templateUrl: './asset-sub-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetSubCategoryUpdateComponent implements OnInit {
  isSaving = false;
  assetSubCategory: IAssetSubCategory | null = null;

  assetCategoriesSharedCollection: IAssetCategory[] = [];

  protected assetSubCategoryService = inject(AssetSubCategoryService);
  protected assetSubCategoryFormService = inject(AssetSubCategoryFormService);
  protected assetCategoryService = inject(AssetCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetSubCategoryFormGroup = this.assetSubCategoryFormService.createAssetSubCategoryFormGroup();

  compareAssetCategory = (o1: IAssetCategory | null, o2: IAssetCategory | null): boolean =>
    this.assetCategoryService.compareAssetCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetSubCategory }) => {
      this.assetSubCategory = assetSubCategory;
      if (assetSubCategory) {
        this.updateForm(assetSubCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetSubCategory = this.assetSubCategoryFormService.getAssetSubCategory(this.editForm);
    if (assetSubCategory.id !== null) {
      this.subscribeToSaveResponse(this.assetSubCategoryService.update(assetSubCategory));
    } else {
      this.subscribeToSaveResponse(this.assetSubCategoryService.create(assetSubCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetSubCategory>>): void {
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

  protected updateForm(assetSubCategory: IAssetSubCategory): void {
    this.assetSubCategory = assetSubCategory;
    this.assetSubCategoryFormService.resetForm(this.editForm, assetSubCategory);

    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing<IAssetCategory>(
      this.assetCategoriesSharedCollection,
      assetSubCategory.category,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing<IAssetCategory>(assetCategories, this.assetSubCategory?.category),
        ),
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));
  }
}
