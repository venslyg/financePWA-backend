import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../liability-log.test-samples';

import { LiabilityLogFormService } from './liability-log-form.service';

describe('LiabilityLog Form Service', () => {
  let service: LiabilityLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LiabilityLogFormService);
  });

  describe('Service methods', () => {
    describe('createLiabilityLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLiabilityLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            liabilityCode: expect.any(Object),
            loanFrom: expect.any(Object),
            description: expect.any(Object),
            liabilityType: expect.any(Object),
            totalLoanAmount: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            interestPercentage: expect.any(Object),
            monthlyPaymentAmount: expect.any(Object),
            principalPaid: expect.any(Object),
            balanceToPay: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing ILiabilityLog should create a new form with FormGroup', () => {
        const formGroup = service.createLiabilityLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            liabilityCode: expect.any(Object),
            loanFrom: expect.any(Object),
            description: expect.any(Object),
            liabilityType: expect.any(Object),
            totalLoanAmount: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            interestPercentage: expect.any(Object),
            monthlyPaymentAmount: expect.any(Object),
            principalPaid: expect.any(Object),
            balanceToPay: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getLiabilityLog', () => {
      it('should return NewLiabilityLog for default LiabilityLog initial value', () => {
        const formGroup = service.createLiabilityLogFormGroup(sampleWithNewData);

        const liabilityLog = service.getLiabilityLog(formGroup) as any;

        expect(liabilityLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewLiabilityLog for empty LiabilityLog initial value', () => {
        const formGroup = service.createLiabilityLogFormGroup();

        const liabilityLog = service.getLiabilityLog(formGroup) as any;

        expect(liabilityLog).toMatchObject({});
      });

      it('should return ILiabilityLog', () => {
        const formGroup = service.createLiabilityLogFormGroup(sampleWithRequiredData);

        const liabilityLog = service.getLiabilityLog(formGroup) as any;

        expect(liabilityLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILiabilityLog should not enable id FormControl', () => {
        const formGroup = service.createLiabilityLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLiabilityLog should disable id FormControl', () => {
        const formGroup = service.createLiabilityLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
