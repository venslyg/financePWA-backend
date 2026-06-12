import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDonationTracker } from '../donation-tracker.model';
import { DonationTrackerService } from '../service/donation-tracker.service';

@Component({
  templateUrl: './donation-tracker-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DonationTrackerDeleteDialogComponent {
  donationTracker?: IDonationTracker;

  protected donationTrackerService = inject(DonationTrackerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donationTrackerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
