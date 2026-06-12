import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../salary-payout.test-samples';

import { SalaryPayoutFormService } from './salary-payout-form.service';

describe('SalaryPayout Form Service', () => {
  let service: SalaryPayoutFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryPayoutFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryPayoutFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryPayoutFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            salaryPayoutCode: expect.any(Object),
            staffCode: expect.any(Object),
            payPeriod: expect.any(Object),
            baseSalary: expect.any(Object),
            allowances: expect.any(Object),
            deductions: expect.any(Object),
            netPay: expect.any(Object),
            payoutDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing ISalaryPayout should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryPayoutFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            salaryPayoutCode: expect.any(Object),
            staffCode: expect.any(Object),
            payPeriod: expect.any(Object),
            baseSalary: expect.any(Object),
            allowances: expect.any(Object),
            deductions: expect.any(Object),
            netPay: expect.any(Object),
            payoutDate: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getSalaryPayout', () => {
      it('should return NewSalaryPayout for default SalaryPayout initial value', () => {
        const formGroup = service.createSalaryPayoutFormGroup(sampleWithNewData);

        const salaryPayout = service.getSalaryPayout(formGroup) as any;

        expect(salaryPayout).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalaryPayout for empty SalaryPayout initial value', () => {
        const formGroup = service.createSalaryPayoutFormGroup();

        const salaryPayout = service.getSalaryPayout(formGroup) as any;

        expect(salaryPayout).toMatchObject({});
      });

      it('should return ISalaryPayout', () => {
        const formGroup = service.createSalaryPayoutFormGroup(sampleWithRequiredData);

        const salaryPayout = service.getSalaryPayout(formGroup) as any;

        expect(salaryPayout).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalaryPayout should not enable id FormControl', () => {
        const formGroup = service.createSalaryPayoutFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalaryPayout should disable id FormControl', () => {
        const formGroup = service.createSalaryPayoutFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
