import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IIncomeEntry } from '../income-entry.model';

@Component({
  selector: 'jhi-income-entry-detail',
  templateUrl: './income-entry-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class IncomeEntryDetailComponent {
  incomeEntry = input<IIncomeEntry | null>(null);

  previousState(): void {
    window.history.back();
  }
}
