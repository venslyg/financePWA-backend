import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bin-card-line.test-samples';

import { BinCardLineFormService } from './bin-card-line-form.service';

describe('BinCardLine Form Service', () => {
  let service: BinCardLineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BinCardLineFormService);
  });

  describe('Service methods', () => {
    describe('createBinCardLineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBinCardLineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inventoryItemCode: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            description: expect.any(Object),
            quantityIn: expect.any(Object),
            quantityOut: expect.any(Object),
            runningBalance: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IBinCardLine should create a new form with FormGroup', () => {
        const formGroup = service.createBinCardLineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inventoryItemCode: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            description: expect.any(Object),
            quantityIn: expect.any(Object),
            quantityOut: expect.any(Object),
            runningBalance: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBinCardLine', () => {
      it('should return NewBinCardLine for default BinCardLine initial value', () => {
        const formGroup = service.createBinCardLineFormGroup(sampleWithNewData);

        const binCardLine = service.getBinCardLine(formGroup) as any;

        expect(binCardLine).toMatchObject(sampleWithNewData);
      });

      it('should return NewBinCardLine for empty BinCardLine initial value', () => {
        const formGroup = service.createBinCardLineFormGroup();

        const binCardLine = service.getBinCardLine(formGroup) as any;

        expect(binCardLine).toMatchObject({});
      });

      it('should return IBinCardLine', () => {
        const formGroup = service.createBinCardLineFormGroup(sampleWithRequiredData);

        const binCardLine = service.getBinCardLine(formGroup) as any;

        expect(binCardLine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBinCardLine should not enable id FormControl', () => {
        const formGroup = service.createBinCardLineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBinCardLine should disable id FormControl', () => {
        const formGroup = service.createBinCardLineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
