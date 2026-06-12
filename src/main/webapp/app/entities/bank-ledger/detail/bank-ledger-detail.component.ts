import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBankLedger } from '../bank-ledger.model';

@Component({
  selector: 'jhi-bank-ledger-detail',
  templateUrl: './bank-ledger-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BankLedgerDetailComponent {
  bankLedger = input<IBankLedger | null>(null);

  previousState(): void {
    window.history.back();
  }
}
