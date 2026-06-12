import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../expense-category.test-samples';

import { ExpenseCategoryFormService } from './expense-category-form.service';

describe('ExpenseCategory Form Service', () => {
  let service: ExpenseCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoryCode: expect.any(Object),
            categoryName: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IExpenseCategory should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoryCode: expect.any(Object),
            categoryName: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getExpenseCategory', () => {
      it('should return NewExpenseCategory for default ExpenseCategory initial value', () => {
        const formGroup = service.createExpenseCategoryFormGroup(sampleWithNewData);

        const expenseCategory = service.getExpenseCategory(formGroup) as any;

        expect(expenseCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseCategory for empty ExpenseCategory initial value', () => {
        const formGroup = service.createExpenseCategoryFormGroup();

        const expenseCategory = service.getExpenseCategory(formGroup) as any;

        expect(expenseCategory).toMatchObject({});
      });

      it('should return IExpenseCategory', () => {
        const formGroup = service.createExpenseCategoryFormGroup(sampleWithRequiredData);

        const expenseCategory = service.getExpenseCategory(formGroup) as any;

        expect(expenseCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseCategory should not enable id FormControl', () => {
        const formGroup = service.createExpenseCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseCategory should disable id FormControl', () => {
        const formGroup = service.createExpenseCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
