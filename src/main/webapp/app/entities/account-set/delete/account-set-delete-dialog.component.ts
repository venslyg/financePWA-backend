import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountSet } from '../account-set.model';
import { AccountSetService } from '../service/account-set.service';

@Component({
  templateUrl: './account-set-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountSetDeleteDialogComponent {
  accountSet?: IAccountSet;

  protected accountSetService = inject(AccountSetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountSetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
