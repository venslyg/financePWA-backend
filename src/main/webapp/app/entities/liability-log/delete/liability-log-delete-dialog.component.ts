import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILiabilityLog } from '../liability-log.model';
import { LiabilityLogService } from '../service/liability-log.service';

@Component({
  templateUrl: './liability-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LiabilityLogDeleteDialogComponent {
  liabilityLog?: ILiabilityLog;

  protected liabilityLogService = inject(LiabilityLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.liabilityLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
