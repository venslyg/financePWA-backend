import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IExpenseEntry } from '../expense-entry.model';

@Component({
  selector: 'jhi-expense-entry-detail',
  templateUrl: './expense-entry-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExpenseEntryDetailComponent {
  expenseEntry = input<IExpenseEntry | null>(null);

  previousState(): void {
    window.history.back();
  }
}
