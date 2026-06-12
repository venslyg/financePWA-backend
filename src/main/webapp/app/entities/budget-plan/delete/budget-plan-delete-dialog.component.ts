import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBudgetPlan } from '../budget-plan.model';
import { BudgetPlanService } from '../service/budget-plan.service';

@Component({
  templateUrl: './budget-plan-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BudgetPlanDeleteDialogComponent {
  budgetPlan?: IBudgetPlan;

  protected budgetPlanService = inject(BudgetPlanService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.budgetPlanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
