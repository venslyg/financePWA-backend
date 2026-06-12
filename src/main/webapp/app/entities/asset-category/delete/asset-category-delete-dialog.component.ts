import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';

@Component({
  templateUrl: './asset-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetCategoryDeleteDialogComponent {
  assetCategory?: IAssetCategory;

  protected assetCategoryService = inject(AssetCategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
