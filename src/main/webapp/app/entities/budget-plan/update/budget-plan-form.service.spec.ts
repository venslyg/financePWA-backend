import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../budget-plan.test-samples';

import { BudgetPlanFormService } from './budget-plan-form.service';

describe('BudgetPlan Form Service', () => {
  let service: BudgetPlanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BudgetPlanFormService);
  });

  describe('Service methods', () => {
    describe('createBudgetPlanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBudgetPlanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            budgetPlanCode: expect.any(Object),
            departmentName: expect.any(Object),
            year: expect.any(Object),
            allocatedAmount: expect.any(Object),
            spentAmount: expect.any(Object),
            remainingAmount: expect.any(Object),
            usedPercentage: expect.any(Object),
            alertStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IBudgetPlan should create a new form with FormGroup', () => {
        const formGroup = service.createBudgetPlanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            budgetPlanCode: expect.any(Object),
            departmentName: expect.any(Object),
            year: expect.any(Object),
            allocatedAmount: expect.any(Object),
            spentAmount: expect.any(Object),
            remainingAmount: expect.any(Object),
            usedPercentage: expect.any(Object),
            alertStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBudgetPlan', () => {
      it('should return NewBudgetPlan for default BudgetPlan initial value', () => {
        const formGroup = service.createBudgetPlanFormGroup(sampleWithNewData);

        const budgetPlan = service.getBudgetPlan(formGroup) as any;

        expect(budgetPlan).toMatchObject(sampleWithNewData);
      });

      it('should return NewBudgetPlan for empty BudgetPlan initial value', () => {
        const formGroup = service.createBudgetPlanFormGroup();

        const budgetPlan = service.getBudgetPlan(formGroup) as any;

        expect(budgetPlan).toMatchObject({});
      });

      it('should return IBudgetPlan', () => {
        const formGroup = service.createBudgetPlanFormGroup(sampleWithRequiredData);

        const budgetPlan = service.getBudgetPlan(formGroup) as any;

        expect(budgetPlan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBudgetPlan should not enable id FormControl', () => {
        const formGroup = service.createBudgetPlanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBudgetPlan should disable id FormControl', () => {
        const formGroup = service.createBudgetPlanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
