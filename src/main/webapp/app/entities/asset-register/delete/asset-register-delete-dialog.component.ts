import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetRegister } from '../asset-register.model';
import { AssetRegisterService } from '../service/asset-register.service';

@Component({
  templateUrl: './asset-register-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetRegisterDeleteDialogComponent {
  assetRegister?: IAssetRegister;

  protected assetRegisterService = inject(AssetRegisterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetRegisterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
