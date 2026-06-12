import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAssetSubCategory } from '../asset-sub-category.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../asset-sub-category.test-samples';

import { AssetSubCategoryService, RestAssetSubCategory } from './asset-sub-category.service';

const requireRestSample: RestAssetSubCategory = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('AssetSubCategory Service', () => {
  let service: AssetSubCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IAssetSubCategory | IAssetSubCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AssetSubCategoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AssetSubCategory', () => {
      const assetSubCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(assetSubCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetSubCategory', () => {
      const assetSubCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(assetSubCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetSubCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetSubCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AssetSubCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AssetSubCategory', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addAssetSubCategoryToCollectionIfMissing', () => {
      it('should add a AssetSubCategory to an empty array', () => {
        const assetSubCategory: IAssetSubCategory = sampleWithRequiredData;
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing([], assetSubCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetSubCategory);
      });

      it('should not add a AssetSubCategory to an array that contains it', () => {
        const assetSubCategory: IAssetSubCategory = sampleWithRequiredData;
        const assetSubCategoryCollection: IAssetSubCategory[] = [
          {
            ...assetSubCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing(assetSubCategoryCollection, assetSubCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetSubCategory to an array that doesn't contain it", () => {
        const assetSubCategory: IAssetSubCategory = sampleWithRequiredData;
        const assetSubCategoryCollection: IAssetSubCategory[] = [sampleWithPartialData];
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing(assetSubCategoryCollection, assetSubCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetSubCategory);
      });

      it('should add only unique AssetSubCategory to an array', () => {
        const assetSubCategoryArray: IAssetSubCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const assetSubCategoryCollection: IAssetSubCategory[] = [sampleWithRequiredData];
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing(assetSubCategoryCollection, ...assetSubCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetSubCategory: IAssetSubCategory = sampleWithRequiredData;
        const assetSubCategory2: IAssetSubCategory = sampleWithPartialData;
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing([], assetSubCategory, assetSubCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetSubCategory);
        expect(expectedResult).toContain(assetSubCategory2);
      });

      it('should accept null and undefined values', () => {
        const assetSubCategory: IAssetSubCategory = sampleWithRequiredData;
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing([], null, assetSubCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetSubCategory);
      });

      it('should return initial array if no AssetSubCategory is added', () => {
        const assetSubCategoryCollection: IAssetSubCategory[] = [sampleWithRequiredData];
        expectedResult = service.addAssetSubCategoryToCollectionIfMissing(assetSubCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(assetSubCategoryCollection);
      });
    });

    describe('compareAssetSubCategory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAssetSubCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24169 };
        const entity2 = null;

        const compareResult1 = service.compareAssetSubCategory(entity1, entity2);
        const compareResult2 = service.compareAssetSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24169 };
        const entity2 = { id: 83 };

        const compareResult1 = service.compareAssetSubCategory(entity1, entity2);
        const compareResult2 = service.compareAssetSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24169 };
        const entity2 = { id: 24169 };

        const compareResult1 = service.compareAssetSubCategory(entity1, entity2);
        const compareResult2 = service.compareAssetSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
