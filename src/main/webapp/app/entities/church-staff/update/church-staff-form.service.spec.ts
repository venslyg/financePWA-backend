import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../church-staff.test-samples';

import { ChurchStaffFormService } from './church-staff-form.service';

describe('ChurchStaff Form Service', () => {
  let service: ChurchStaffFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChurchStaffFormService);
  });

  describe('Service methods', () => {
    describe('createChurchStaffFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChurchStaffFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            staffCode: expect.any(Object),
            branchCode: expect.any(Object),
            fullName: expect.any(Object),
            position: expect.any(Object),
            staffType: expect.any(Object),
            contactNumber: expect.any(Object),
            hourlyRateOrMonthlySalary: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IChurchStaff should create a new form with FormGroup', () => {
        const formGroup = service.createChurchStaffFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            staffCode: expect.any(Object),
            branchCode: expect.any(Object),
            fullName: expect.any(Object),
            position: expect.any(Object),
            staffType: expect.any(Object),
            contactNumber: expect.any(Object),
            hourlyRateOrMonthlySalary: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getChurchStaff', () => {
      it('should return NewChurchStaff for default ChurchStaff initial value', () => {
        const formGroup = service.createChurchStaffFormGroup(sampleWithNewData);

        const churchStaff = service.getChurchStaff(formGroup) as any;

        expect(churchStaff).toMatchObject(sampleWithNewData);
      });

      it('should return NewChurchStaff for empty ChurchStaff initial value', () => {
        const formGroup = service.createChurchStaffFormGroup();

        const churchStaff = service.getChurchStaff(formGroup) as any;

        expect(churchStaff).toMatchObject({});
      });

      it('should return IChurchStaff', () => {
        const formGroup = service.createChurchStaffFormGroup(sampleWithRequiredData);

        const churchStaff = service.getChurchStaff(formGroup) as any;

        expect(churchStaff).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChurchStaff should not enable id FormControl', () => {
        const formGroup = service.createChurchStaffFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChurchStaff should disable id FormControl', () => {
        const formGroup = service.createChurchStaffFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
