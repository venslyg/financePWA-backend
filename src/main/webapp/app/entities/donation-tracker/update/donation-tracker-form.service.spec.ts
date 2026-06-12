import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../donation-tracker.test-samples';

import { DonationTrackerFormService } from './donation-tracker-form.service';

describe('DonationTracker Form Service', () => {
  let service: DonationTrackerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DonationTrackerFormService);
  });

  describe('Service methods', () => {
    describe('createDonationTrackerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDonationTrackerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            donationIdCode: expect.any(Object),
            date: expect.any(Object),
            donorNameOrOrg: expect.any(Object),
            contactDetails: expect.any(Object),
            amount: expect.any(Object),
            purpose: expect.any(Object),
            receivedViaMode: expect.any(Object),
            notes: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IDonationTracker should create a new form with FormGroup', () => {
        const formGroup = service.createDonationTrackerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            donationIdCode: expect.any(Object),
            date: expect.any(Object),
            donorNameOrOrg: expect.any(Object),
            contactDetails: expect.any(Object),
            amount: expect.any(Object),
            purpose: expect.any(Object),
            receivedViaMode: expect.any(Object),
            notes: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getDonationTracker', () => {
      it('should return NewDonationTracker for default DonationTracker initial value', () => {
        const formGroup = service.createDonationTrackerFormGroup(sampleWithNewData);

        const donationTracker = service.getDonationTracker(formGroup) as any;

        expect(donationTracker).toMatchObject(sampleWithNewData);
      });

      it('should return NewDonationTracker for empty DonationTracker initial value', () => {
        const formGroup = service.createDonationTrackerFormGroup();

        const donationTracker = service.getDonationTracker(formGroup) as any;

        expect(donationTracker).toMatchObject({});
      });

      it('should return IDonationTracker', () => {
        const formGroup = service.createDonationTrackerFormGroup(sampleWithRequiredData);

        const donationTracker = service.getDonationTracker(formGroup) as any;

        expect(donationTracker).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDonationTracker should not enable id FormControl', () => {
        const formGroup = service.createDonationTrackerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDonationTracker should disable id FormControl', () => {
        const formGroup = service.createDonationTrackerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
