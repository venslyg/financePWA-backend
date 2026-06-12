import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ILiabilityLog } from '../liability-log.model';

@Component({
  selector: 'jhi-liability-log-detail',
  templateUrl: './liability-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LiabilityLogDetailComponent {
  liabilityLog = input<ILiabilityLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
