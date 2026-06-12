import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IExpenseSubCategory } from '../expense-sub-category.model';

@Component({
  selector: 'jhi-expense-sub-category-detail',
  templateUrl: './expense-sub-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ExpenseSubCategoryDetailComponent {
  expenseSubCategory = input<IExpenseSubCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
