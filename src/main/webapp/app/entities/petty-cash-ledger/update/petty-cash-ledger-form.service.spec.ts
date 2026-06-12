import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../petty-cash-ledger.test-samples';

import { PettyCashLedgerFormService } from './petty-cash-ledger-form.service';

describe('PettyCashLedger Form Service', () => {
  let service: PettyCashLedgerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PettyCashLedgerFormService);
  });

  describe('Service methods', () => {
    describe('createPettyCashLedgerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPettyCashLedgerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            pettyCashCode: expect.any(Object),
            date: expect.any(Object),
            pettyCashVoucherNo: expect.any(Object),
            description: expect.any(Object),
            cashIn: expect.any(Object),
            cashOut: expect.any(Object),
            runningBalance: expect.any(Object),
            linkedAccountCode: expect.any(Object),
            referenceNo: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IPettyCashLedger should create a new form with FormGroup', () => {
        const formGroup = service.createPettyCashLedgerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            pettyCashCode: expect.any(Object),
            date: expect.any(Object),
            pettyCashVoucherNo: expect.any(Object),
            description: expect.any(Object),
            cashIn: expect.any(Object),
            cashOut: expect.any(Object),
            runningBalance: expect.any(Object),
            linkedAccountCode: expect.any(Object),
            referenceNo: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getPettyCashLedger', () => {
      it('should return NewPettyCashLedger for default PettyCashLedger initial value', () => {
        const formGroup = service.createPettyCashLedgerFormGroup(sampleWithNewData);

        const pettyCashLedger = service.getPettyCashLedger(formGroup) as any;

        expect(pettyCashLedger).toMatchObject(sampleWithNewData);
      });

      it('should return NewPettyCashLedger for empty PettyCashLedger initial value', () => {
        const formGroup = service.createPettyCashLedgerFormGroup();

        const pettyCashLedger = service.getPettyCashLedger(formGroup) as any;

        expect(pettyCashLedger).toMatchObject({});
      });

      it('should return IPettyCashLedger', () => {
        const formGroup = service.createPettyCashLedgerFormGroup(sampleWithRequiredData);

        const pettyCashLedger = service.getPettyCashLedger(formGroup) as any;

        expect(pettyCashLedger).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPettyCashLedger should not enable id FormControl', () => {
        const formGroup = service.createPettyCashLedgerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPettyCashLedger should disable id FormControl', () => {
        const formGroup = service.createPettyCashLedgerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
