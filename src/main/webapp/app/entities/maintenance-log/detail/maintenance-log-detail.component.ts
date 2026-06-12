import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMaintenanceLog } from '../maintenance-log.model';

@Component({
  selector: 'jhi-maintenance-log-detail',
  templateUrl: './maintenance-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaintenanceLogDetailComponent {
  maintenanceLog = input<IMaintenanceLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
