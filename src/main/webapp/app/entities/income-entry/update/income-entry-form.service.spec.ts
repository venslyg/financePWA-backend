import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../income-entry.test-samples';

import { IncomeEntryFormService } from './income-entry-form.service';

describe('IncomeEntry Form Service', () => {
  let service: IncomeEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncomeEntryFormService);
  });

  describe('Service methods', () => {
    describe('createIncomeEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncomeEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            incomeCode: expect.any(Object),
            createdByUsername: expect.any(Object),
            date: expect.any(Object),
            receiptNo: expect.any(Object),
            description: expect.any(Object),
            incomeType: expect.any(Object),
            amount: expect.any(Object),
            paymentMethod: expect.any(Object),
            receivablePerson: expect.any(Object),
            receivedBy: expect.any(Object),
            syncStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IIncomeEntry should create a new form with FormGroup', () => {
        const formGroup = service.createIncomeEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            accountCode: expect.any(Object),
            incomeCode: expect.any(Object),
            createdByUsername: expect.any(Object),
            date: expect.any(Object),
            receiptNo: expect.any(Object),
            description: expect.any(Object),
            incomeType: expect.any(Object),
            amount: expect.any(Object),
            paymentMethod: expect.any(Object),
            receivablePerson: expect.any(Object),
            receivedBy: expect.any(Object),
            syncStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getIncomeEntry', () => {
      it('should return NewIncomeEntry for default IncomeEntry initial value', () => {
        const formGroup = service.createIncomeEntryFormGroup(sampleWithNewData);

        const incomeEntry = service.getIncomeEntry(formGroup) as any;

        expect(incomeEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncomeEntry for empty IncomeEntry initial value', () => {
        const formGroup = service.createIncomeEntryFormGroup();

        const incomeEntry = service.getIncomeEntry(formGroup) as any;

        expect(incomeEntry).toMatchObject({});
      });

      it('should return IIncomeEntry', () => {
        const formGroup = service.createIncomeEntryFormGroup(sampleWithRequiredData);

        const incomeEntry = service.getIncomeEntry(formGroup) as any;

        expect(incomeEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncomeEntry should not enable id FormControl', () => {
        const formGroup = service.createIncomeEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncomeEntry should disable id FormControl', () => {
        const formGroup = service.createIncomeEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
