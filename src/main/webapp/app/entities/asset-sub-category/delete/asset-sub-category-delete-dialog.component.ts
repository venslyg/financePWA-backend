import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetSubCategory } from '../asset-sub-category.model';
import { AssetSubCategoryService } from '../service/asset-sub-category.service';

@Component({
  templateUrl: './asset-sub-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetSubCategoryDeleteDialogComponent {
  assetSubCategory?: IAssetSubCategory;

  protected assetSubCategoryService = inject(AssetSubCategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetSubCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
