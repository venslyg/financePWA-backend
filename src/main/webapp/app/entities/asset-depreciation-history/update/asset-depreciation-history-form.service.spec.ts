import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../asset-depreciation-history.test-samples';

import { AssetDepreciationHistoryFormService } from './asset-depreciation-history-form.service';

describe('AssetDepreciationHistory Form Service', () => {
  let service: AssetDepreciationHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetDepreciationHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createAssetDepreciationHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assetRegisterCode: expect.any(Object),
            depreciationDate: expect.any(Object),
            depreciationAmount: expect.any(Object),
            valueAfterDepreciation: expect.any(Object),
            processedBy: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IAssetDepreciationHistory should create a new form with FormGroup', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assetRegisterCode: expect.any(Object),
            depreciationDate: expect.any(Object),
            depreciationAmount: expect.any(Object),
            valueAfterDepreciation: expect.any(Object),
            processedBy: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetDepreciationHistory', () => {
      it('should return NewAssetDepreciationHistory for default AssetDepreciationHistory initial value', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup(sampleWithNewData);

        const assetDepreciationHistory = service.getAssetDepreciationHistory(formGroup) as any;

        expect(assetDepreciationHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetDepreciationHistory for empty AssetDepreciationHistory initial value', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup();

        const assetDepreciationHistory = service.getAssetDepreciationHistory(formGroup) as any;

        expect(assetDepreciationHistory).toMatchObject({});
      });

      it('should return IAssetDepreciationHistory', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup(sampleWithRequiredData);

        const assetDepreciationHistory = service.getAssetDepreciationHistory(formGroup) as any;

        expect(assetDepreciationHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetDepreciationHistory should not enable id FormControl', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetDepreciationHistory should disable id FormControl', () => {
        const formGroup = service.createAssetDepreciationHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
