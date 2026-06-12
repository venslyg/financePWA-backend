import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bank-ledger.test-samples';

import { BankLedgerFormService } from './bank-ledger-form.service';

describe('BankLedger Form Service', () => {
  let service: BankLedgerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankLedgerFormService);
  });

  describe('Service methods', () => {
    describe('createBankLedgerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBankLedgerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            bankLedgerCode: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            description: expect.any(Object),
            depositAmount: expect.any(Object),
            withdrawalAmount: expect.any(Object),
            runningBalance: expect.any(Object),
            remark: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IBankLedger should create a new form with FormGroup', () => {
        const formGroup = service.createBankLedgerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            bankLedgerCode: expect.any(Object),
            date: expect.any(Object),
            referenceNo: expect.any(Object),
            description: expect.any(Object),
            depositAmount: expect.any(Object),
            withdrawalAmount: expect.any(Object),
            runningBalance: expect.any(Object),
            remark: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBankLedger', () => {
      it('should return NewBankLedger for default BankLedger initial value', () => {
        const formGroup = service.createBankLedgerFormGroup(sampleWithNewData);

        const bankLedger = service.getBankLedger(formGroup) as any;

        expect(bankLedger).toMatchObject(sampleWithNewData);
      });

      it('should return NewBankLedger for empty BankLedger initial value', () => {
        const formGroup = service.createBankLedgerFormGroup();

        const bankLedger = service.getBankLedger(formGroup) as any;

        expect(bankLedger).toMatchObject({});
      });

      it('should return IBankLedger', () => {
        const formGroup = service.createBankLedgerFormGroup(sampleWithRequiredData);

        const bankLedger = service.getBankLedger(formGroup) as any;

        expect(bankLedger).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBankLedger should not enable id FormControl', () => {
        const formGroup = service.createBankLedgerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBankLedger should disable id FormControl', () => {
        const formGroup = service.createBankLedgerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
