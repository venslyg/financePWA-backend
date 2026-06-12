import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPettyCashLedger } from '../petty-cash-ledger.model';

@Component({
  selector: 'jhi-petty-cash-ledger-detail',
  templateUrl: './petty-cash-ledger-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PettyCashLedgerDetailComponent {
  pettyCashLedger = input<IPettyCashLedger | null>(null);

  previousState(): void {
    window.history.back();
  }
}
