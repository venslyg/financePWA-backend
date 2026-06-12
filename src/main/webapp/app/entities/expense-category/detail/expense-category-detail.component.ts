import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IExpenseCategory } from '../expense-category.model';

@Component({
  selector: 'jhi-expense-category-detail',
  templateUrl: './expense-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ExpenseCategoryDetailComponent {
  expenseCategory = input<IExpenseCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
