import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExpenseCategory } from 'app/entities/expense-category/expense-category.model';
import { ExpenseCategoryService } from 'app/entities/expense-category/service/expense-category.service';
import { IExpenseSubCategory } from '../expense-sub-category.model';
import { ExpenseSubCategoryService } from '../service/expense-sub-category.service';
import { ExpenseSubCategoryFormGroup, ExpenseSubCategoryFormService } from './expense-sub-category-form.service';

@Component({
  selector: 'jhi-expense-sub-category-update',
  templateUrl: './expense-sub-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseSubCategoryUpdateComponent implements OnInit {
  isSaving = false;
  expenseSubCategory: IExpenseSubCategory | null = null;

  expenseCategoriesSharedCollection: IExpenseCategory[] = [];

  protected expenseSubCategoryService = inject(ExpenseSubCategoryService);
  protected expenseSubCategoryFormService = inject(ExpenseSubCategoryFormService);
  protected expenseCategoryService = inject(ExpenseCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseSubCategoryFormGroup = this.expenseSubCategoryFormService.createExpenseSubCategoryFormGroup();

  compareExpenseCategory = (o1: IExpenseCategory | null, o2: IExpenseCategory | null): boolean =>
    this.expenseCategoryService.compareExpenseCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseSubCategory }) => {
      this.expenseSubCategory = expenseSubCategory;
      if (expenseSubCategory) {
        this.updateForm(expenseSubCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseSubCategory = this.expenseSubCategoryFormService.getExpenseSubCategory(this.editForm);
    if (expenseSubCategory.id !== null) {
      this.subscribeToSaveResponse(this.expenseSubCategoryService.update(expenseSubCategory));
    } else {
      this.subscribeToSaveResponse(this.expenseSubCategoryService.create(expenseSubCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseSubCategory>>): void {
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

  protected updateForm(expenseSubCategory: IExpenseSubCategory): void {
    this.expenseSubCategory = expenseSubCategory;
    this.expenseSubCategoryFormService.resetForm(this.editForm, expenseSubCategory);

    this.expenseCategoriesSharedCollection = this.expenseCategoryService.addExpenseCategoryToCollectionIfMissing<IExpenseCategory>(
      this.expenseCategoriesSharedCollection,
      expenseSubCategory.category,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.expenseCategoryService
      .query()
      .pipe(map((res: HttpResponse<IExpenseCategory[]>) => res.body ?? []))
      .pipe(
        map((expenseCategories: IExpenseCategory[]) =>
          this.expenseCategoryService.addExpenseCategoryToCollectionIfMissing<IExpenseCategory>(
            expenseCategories,
            this.expenseSubCategory?.category,
          ),
        ),
      )
      .subscribe((expenseCategories: IExpenseCategory[]) => (this.expenseCategoriesSharedCollection = expenseCategories));
  }
}
