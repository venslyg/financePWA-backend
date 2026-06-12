import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BudgetAlertStatus } from 'app/entities/enumerations/budget-alert-status.model';
import { IBudgetPlan } from '../budget-plan.model';
import { BudgetPlanService } from '../service/budget-plan.service';
import { BudgetPlanFormGroup, BudgetPlanFormService } from './budget-plan-form.service';

@Component({
  selector: 'jhi-budget-plan-update',
  templateUrl: './budget-plan-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BudgetPlanUpdateComponent implements OnInit {
  isSaving = false;
  budgetPlan: IBudgetPlan | null = null;
  budgetAlertStatusValues = Object.keys(BudgetAlertStatus);

  protected budgetPlanService = inject(BudgetPlanService);
  protected budgetPlanFormService = inject(BudgetPlanFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BudgetPlanFormGroup = this.budgetPlanFormService.createBudgetPlanFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budgetPlan }) => {
      this.budgetPlan = budgetPlan;
      if (budgetPlan) {
        this.updateForm(budgetPlan);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const budgetPlan = this.budgetPlanFormService.getBudgetPlan(this.editForm);
    if (budgetPlan.id !== null) {
      this.subscribeToSaveResponse(this.budgetPlanService.update(budgetPlan));
    } else {
      this.subscribeToSaveResponse(this.budgetPlanService.create(budgetPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudgetPlan>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(budgetPlan: IBudgetPlan): void {
    this.budgetPlan = budgetPlan;
    this.budgetPlanFormService.resetForm(this.editForm, budgetPlan);
  }
}
