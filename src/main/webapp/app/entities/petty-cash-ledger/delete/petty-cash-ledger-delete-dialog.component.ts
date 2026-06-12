import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPettyCashLedger } from '../petty-cash-ledger.model';
import { PettyCashLedgerService } from '../service/petty-cash-ledger.service';

@Component({
  templateUrl: './petty-cash-ledger-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PettyCashLedgerDeleteDialogComponent {
  pettyCashLedger?: IPettyCashLedger;

  protected pettyCashLedgerService = inject(PettyCashLedgerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pettyCashLedgerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
