import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChurchStaff } from '../church-staff.model';
import { ChurchStaffService } from '../service/church-staff.service';

@Component({
  templateUrl: './church-staff-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChurchStaffDeleteDialogComponent {
  churchStaff?: IChurchStaff;

  protected churchStaffService = inject(ChurchStaffService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.churchStaffService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
