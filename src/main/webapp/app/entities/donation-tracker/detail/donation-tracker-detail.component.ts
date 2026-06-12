import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDonationTracker } from '../donation-tracker.model';

@Component({
  selector: 'jhi-donation-tracker-detail',
  templateUrl: './donation-tracker-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DonationTrackerDetailComponent {
  donationTracker = input<IDonationTracker | null>(null);

  previousState(): void {
    window.history.back();
  }
}
