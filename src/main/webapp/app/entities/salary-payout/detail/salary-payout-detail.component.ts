import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISalaryPayout } from '../salary-payout.model';

@Component({
  selector: 'jhi-salary-payout-detail',
  templateUrl: './salary-payout-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SalaryPayoutDetailComponent {
  salaryPayout = input<ISalaryPayout | null>(null);

  previousState(): void {
    window.history.back();
  }
}
