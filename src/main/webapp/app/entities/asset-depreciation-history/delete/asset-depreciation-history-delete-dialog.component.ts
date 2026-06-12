import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';
import { AssetDepreciationHistoryService } from '../service/asset-depreciation-history.service';

@Component({
  templateUrl: './asset-depreciation-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetDepreciationHistoryDeleteDialogComponent {
  assetDepreciationHistory?: IAssetDepreciationHistory;

  protected assetDepreciationHistoryService = inject(AssetDepreciationHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetDepreciationHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
