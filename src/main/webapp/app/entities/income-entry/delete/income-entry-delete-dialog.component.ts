import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIncomeEntry } from '../income-entry.model';
import { IncomeEntryService } from '../service/income-entry.service';

@Component({
  templateUrl: './income-entry-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IncomeEntryDeleteDialogComponent {
  incomeEntry?: IIncomeEntry;

  protected incomeEntryService = inject(IncomeEntryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomeEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
