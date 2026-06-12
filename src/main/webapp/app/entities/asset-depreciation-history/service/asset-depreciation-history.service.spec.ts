import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../asset-depreciation-history.test-samples';

import { AssetDepreciationHistoryService, RestAssetDepreciationHistory } from './asset-depreciation-history.service';

const requireRestSample: RestAssetDepreciationHistory = {
  ...sampleWithRequiredData,
  depreciationDate: sampleWithRequiredData.depreciationDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('AssetDepreciationHistory Service', () => {
  let service: AssetDepreciationHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IAssetDepreciationHistory | IAssetDepreciationHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AssetDepreciationHistoryService);
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

    it('should create a AssetDepreciationHistory', () => {
      const assetDepreciationHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(assetDepreciationHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetDepreciationHistory', () => {
      const assetDepreciationHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(assetDepreciationHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetDepreciationHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetDepreciationHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AssetDepreciationHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AssetDepreciationHistory', () => {
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

    describe('addAssetDepreciationHistoryToCollectionIfMissing', () => {
      it('should add a AssetDepreciationHistory to an empty array', () => {
        const assetDepreciationHistory: IAssetDepreciationHistory = sampleWithRequiredData;
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing([], assetDepreciationHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDepreciationHistory);
      });

      it('should not add a AssetDepreciationHistory to an array that contains it', () => {
        const assetDepreciationHistory: IAssetDepreciationHistory = sampleWithRequiredData;
        const assetDepreciationHistoryCollection: IAssetDepreciationHistory[] = [
          {
            ...assetDepreciationHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing(
          assetDepreciationHistoryCollection,
          assetDepreciationHistory,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetDepreciationHistory to an array that doesn't contain it", () => {
        const assetDepreciationHistory: IAssetDepreciationHistory = sampleWithRequiredData;
        const assetDepreciationHistoryCollection: IAssetDepreciationHistory[] = [sampleWithPartialData];
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing(
          assetDepreciationHistoryCollection,
          assetDepreciationHistory,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDepreciationHistory);
      });

      it('should add only unique AssetDepreciationHistory to an array', () => {
        const assetDepreciationHistoryArray: IAssetDepreciationHistory[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const assetDepreciationHistoryCollection: IAssetDepreciationHistory[] = [sampleWithRequiredData];
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing(
          assetDepreciationHistoryCollection,
          ...assetDepreciationHistoryArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetDepreciationHistory: IAssetDepreciationHistory = sampleWithRequiredData;
        const assetDepreciationHistory2: IAssetDepreciationHistory = sampleWithPartialData;
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing([], assetDepreciationHistory, assetDepreciationHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDepreciationHistory);
        expect(expectedResult).toContain(assetDepreciationHistory2);
      });

      it('should accept null and undefined values', () => {
        const assetDepreciationHistory: IAssetDepreciationHistory = sampleWithRequiredData;
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing([], null, assetDepreciationHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDepreciationHistory);
      });

      it('should return initial array if no AssetDepreciationHistory is added', () => {
        const assetDepreciationHistoryCollection: IAssetDepreciationHistory[] = [sampleWithRequiredData];
        expectedResult = service.addAssetDepreciationHistoryToCollectionIfMissing(assetDepreciationHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(assetDepreciationHistoryCollection);
      });
    });

    describe('compareAssetDepreciationHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAssetDepreciationHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23291 };
        const entity2 = null;

        const compareResult1 = service.compareAssetDepreciationHistory(entity1, entity2);
        const compareResult2 = service.compareAssetDepreciationHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23291 };
        const entity2 = { id: 23096 };

        const compareResult1 = service.compareAssetDepreciationHistory(entity1, entity2);
        const compareResult2 = service.compareAssetDepreciationHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23291 };
        const entity2 = { id: 23291 };

        const compareResult1 = service.compareAssetDepreciationHistory(entity1, entity2);
        const compareResult2 = service.compareAssetDepreciationHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
