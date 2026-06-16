import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../maintenance-log.test-samples';

import { MaintenanceLogFormService } from './maintenance-log-form.service';

describe('MaintenanceLog Form Service', () => {
  let service: MaintenanceLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaintenanceLogFormService);
  });

  describe('Service methods', () => {
    describe('createMaintenanceLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaintenanceLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            maintenanceLogCode: expect.any(Object),
            logDate: expect.any(Object),
            logType: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
            vendor: expect.any(Object),
            nextServiceDate: expect.any(Object),
            note: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            asset: expect.any(Object),
          }),
        );
      });

      it('passing IMaintenanceLog should create a new form with FormGroup', () => {
        const formGroup = service.createMaintenanceLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            maintenanceLogCode: expect.any(Object),
            logDate: expect.any(Object),
            logType: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
            vendor: expect.any(Object),
            nextServiceDate: expect.any(Object),
            note: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            asset: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaintenanceLog', () => {
      it('should return NewMaintenanceLog for default MaintenanceLog initial value', () => {
        const formGroup = service.createMaintenanceLogFormGroup(sampleWithNewData);

        const maintenanceLog = service.getMaintenanceLog(formGroup) as any;

        expect(maintenanceLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaintenanceLog for empty MaintenanceLog initial value', () => {
        const formGroup = service.createMaintenanceLogFormGroup();

        const maintenanceLog = service.getMaintenanceLog(formGroup) as any;

        expect(maintenanceLog).toMatchObject({});
      });

      it('should return IMaintenanceLog', () => {
        const formGroup = service.createMaintenanceLogFormGroup(sampleWithRequiredData);

        const maintenanceLog = service.getMaintenanceLog(formGroup) as any;

        expect(maintenanceLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaintenanceLog should not enable id FormControl', () => {
        const formGroup = service.createMaintenanceLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaintenanceLog should disable id FormControl', () => {
        const formGroup = service.createMaintenanceLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
