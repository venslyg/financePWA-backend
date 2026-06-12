import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../asset-register.test-samples';

import { AssetRegisterFormService } from './asset-register-form.service';

describe('AssetRegister Form Service', () => {
  let service: AssetRegisterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetRegisterFormService);
  });

  describe('Service methods', () => {
    describe('createAssetRegisterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetRegisterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            assetRegisterCode: expect.any(Object),
            assetCategoryCode: expect.any(Object),
            assetSubCategoryCode: expect.any(Object),
            assetName: expect.any(Object),
            category: expect.any(Object),
            purchaseDate: expect.any(Object),
            purchaseCost: expect.any(Object),
            currentValue: expect.any(Object),
            depreciationRate: expect.any(Object),
            accumulatedDepreciation: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IAssetRegister should create a new form with FormGroup', () => {
        const formGroup = service.createAssetRegisterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            assetRegisterCode: expect.any(Object),
            assetCategoryCode: expect.any(Object),
            assetSubCategoryCode: expect.any(Object),
            assetName: expect.any(Object),
            category: expect.any(Object),
            purchaseDate: expect.any(Object),
            purchaseCost: expect.any(Object),
            currentValue: expect.any(Object),
            depreciationRate: expect.any(Object),
            accumulatedDepreciation: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetRegister', () => {
      it('should return NewAssetRegister for default AssetRegister initial value', () => {
        const formGroup = service.createAssetRegisterFormGroup(sampleWithNewData);

        const assetRegister = service.getAssetRegister(formGroup) as any;

        expect(assetRegister).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetRegister for empty AssetRegister initial value', () => {
        const formGroup = service.createAssetRegisterFormGroup();

        const assetRegister = service.getAssetRegister(formGroup) as any;

        expect(assetRegister).toMatchObject({});
      });

      it('should return IAssetRegister', () => {
        const formGroup = service.createAssetRegisterFormGroup(sampleWithRequiredData);

        const assetRegister = service.getAssetRegister(formGroup) as any;

        expect(assetRegister).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetRegister should not enable id FormControl', () => {
        const formGroup = service.createAssetRegisterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetRegister should disable id FormControl', () => {
        const formGroup = service.createAssetRegisterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
