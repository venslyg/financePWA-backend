import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISalaryPayout } from '../salary-payout.model';
import { SalaryPayoutService } from '../service/salary-payout.service';

@Component({
  templateUrl: './salary-payout-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SalaryPayoutDeleteDialogComponent {
  salaryPayout?: ISalaryPayout;

  protected salaryPayoutService = inject(SalaryPayoutService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryPayoutService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
