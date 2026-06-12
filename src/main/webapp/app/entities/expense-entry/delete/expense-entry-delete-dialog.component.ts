import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpenseEntry } from '../expense-entry.model';
import { ExpenseEntryService } from '../service/expense-entry.service';

@Component({
  templateUrl: './expense-entry-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpenseEntryDeleteDialogComponent {
  expenseEntry?: IExpenseEntry;

  protected expenseEntryService = inject(ExpenseEntryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
