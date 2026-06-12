import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpenseSubCategory } from '../expense-sub-category.model';
import { ExpenseSubCategoryService } from '../service/expense-sub-category.service';

@Component({
  templateUrl: './expense-sub-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpenseSubCategoryDeleteDialogComponent {
  expenseSubCategory?: IExpenseSubCategory;

  protected expenseSubCategoryService = inject(ExpenseSubCategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseSubCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
