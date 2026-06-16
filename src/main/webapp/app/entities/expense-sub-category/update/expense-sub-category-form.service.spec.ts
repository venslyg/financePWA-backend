import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../expense-sub-category.test-samples';

import { ExpenseSubCategoryFormService } from './expense-sub-category-form.service';

describe('ExpenseSubCategory Form Service', () => {
  let service: ExpenseSubCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseSubCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseSubCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            categoryCode: expect.any(Object),
            subCategoryCode: expect.any(Object),
            subCategoryName: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });

      it('passing IExpenseSubCategory should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            categoryCode: expect.any(Object),
            subCategoryCode: expect.any(Object),
            subCategoryName: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });
    });

    describe('getExpenseSubCategory', () => {
      it('should return NewExpenseSubCategory for default ExpenseSubCategory initial value', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup(sampleWithNewData);

        const expenseSubCategory = service.getExpenseSubCategory(formGroup) as any;

        expect(expenseSubCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseSubCategory for empty ExpenseSubCategory initial value', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup();

        const expenseSubCategory = service.getExpenseSubCategory(formGroup) as any;

        expect(expenseSubCategory).toMatchObject({});
      });

      it('should return IExpenseSubCategory', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup(sampleWithRequiredData);

        const expenseSubCategory = service.getExpenseSubCategory(formGroup) as any;

        expect(expenseSubCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseSubCategory should not enable id FormControl', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseSubCategory should disable id FormControl', () => {
        const formGroup = service.createExpenseSubCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
