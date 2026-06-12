import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IChurchStaff } from '../church-staff.model';

@Component({
  selector: 'jhi-church-staff-detail',
  templateUrl: './church-staff-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ChurchStaffDetailComponent {
  churchStaff = input<IChurchStaff | null>(null);

  previousState(): void {
    window.history.back();
  }
}
