import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../asset-sub-category.test-samples';

import { AssetSubCategoryFormService } from './asset-sub-category-form.service';

describe('AssetSubCategory Form Service', () => {
  let service: AssetSubCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetSubCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createAssetSubCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetSubCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            assetCategoryCode: expect.any(Object),
            assetSubCategoryCode: expect.any(Object),
            assetSubCategoryName: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });

      it('passing IAssetSubCategory should create a new form with FormGroup', () => {
        const formGroup = service.createAssetSubCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            branchCode: expect.any(Object),
            branchId: expect.any(Object),
            assetCategoryCode: expect.any(Object),
            assetSubCategoryCode: expect.any(Object),
            assetSubCategoryName: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetSubCategory', () => {
      it('should return NewAssetSubCategory for default AssetSubCategory initial value', () => {
        const formGroup = service.createAssetSubCategoryFormGroup(sampleWithNewData);

        const assetSubCategory = service.getAssetSubCategory(formGroup) as any;

        expect(assetSubCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetSubCategory for empty AssetSubCategory initial value', () => {
        const formGroup = service.createAssetSubCategoryFormGroup();

        const assetSubCategory = service.getAssetSubCategory(formGroup) as any;

        expect(assetSubCategory).toMatchObject({});
      });

      it('should return IAssetSubCategory', () => {
        const formGroup = service.createAssetSubCategoryFormGroup(sampleWithRequiredData);

        const assetSubCategory = service.getAssetSubCategory(formGroup) as any;

        expect(assetSubCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetSubCategory should not enable id FormControl', () => {
        const formGroup = service.createAssetSubCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetSubCategory should disable id FormControl', () => {
        const formGroup = service.createAssetSubCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
