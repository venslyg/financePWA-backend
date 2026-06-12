import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../expense-entry.test-samples';

import { ExpenseEntryFormService } from './expense-entry-form.service';

describe('ExpenseEntry Form Service', () => {
  let service: ExpenseEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseEntryFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            expenseCode: expect.any(Object),
            expenseCategoryCode: expect.any(Object),
            expenseSubCategoryCode: expect.any(Object),
            createdByUsername: expect.any(Object),
            date: expect.any(Object),
            voucherNo: expect.any(Object),
            description: expect.any(Object),
            amount: expect.any(Object),
            paymentMode: expect.any(Object),
            approvalStatus: expect.any(Object),
            approvedBy: expect.any(Object),
            vendor: expect.any(Object),
            syncStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IExpenseEntry should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            expenseCode: expect.any(Object),
            expenseCategoryCode: expect.any(Object),
            expenseSubCategoryCode: expect.any(Object),
            createdByUsername: expect.any(Object),
            date: expect.any(Object),
            voucherNo: expect.any(Object),
            description: expect.any(Object),
            amount: expect.any(Object),
            paymentMode: expect.any(Object),
            approvalStatus: expect.any(Object),
            approvedBy: expect.any(Object),
            vendor: expect.any(Object),
            syncStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getExpenseEntry', () => {
      it('should return NewExpenseEntry for default ExpenseEntry initial value', () => {
        const formGroup = service.createExpenseEntryFormGroup(sampleWithNewData);

        const expenseEntry = service.getExpenseEntry(formGroup) as any;

        expect(expenseEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseEntry for empty ExpenseEntry initial value', () => {
        const formGroup = service.createExpenseEntryFormGroup();

        const expenseEntry = service.getExpenseEntry(formGroup) as any;

        expect(expenseEntry).toMatchObject({});
      });

      it('should return IExpenseEntry', () => {
        const formGroup = service.createExpenseEntryFormGroup(sampleWithRequiredData);

        const expenseEntry = service.getExpenseEntry(formGroup) as any;

        expect(expenseEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseEntry should not enable id FormControl', () => {
        const formGroup = service.createExpenseEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseEntry should disable id FormControl', () => {
        const formGroup = service.createExpenseEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
