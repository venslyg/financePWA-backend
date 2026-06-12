import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBankLedger } from '../bank-ledger.model';
import { BankLedgerService } from '../service/bank-ledger.service';

@Component({
  templateUrl: './bank-ledger-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BankLedgerDeleteDialogComponent {
  bankLedger?: IBankLedger;

  protected bankLedgerService = inject(BankLedgerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankLedgerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
