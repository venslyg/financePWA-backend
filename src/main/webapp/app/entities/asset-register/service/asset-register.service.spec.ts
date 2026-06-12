import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAssetRegister } from '../asset-register.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../asset-register.test-samples';

import { AssetRegisterService, RestAssetRegister } from './asset-register.service';

const requireRestSample: RestAssetRegister = {
  ...sampleWithRequiredData,
  purchaseDate: sampleWithRequiredData.purchaseDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('AssetRegister Service', () => {
  let service: AssetRegisterService;
  let httpMock: HttpTestingController;
  let expectedResult: IAssetRegister | IAssetRegister[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AssetRegisterService);
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

    it('should create a AssetRegister', () => {
      const assetRegister = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(assetRegister).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetRegister', () => {
      const assetRegister = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(assetRegister).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetRegister', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetRegister', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AssetRegister', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AssetRegister', () => {
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

    describe('addAssetRegisterToCollectionIfMissing', () => {
      it('should add a AssetRegister to an empty array', () => {
        const assetRegister: IAssetRegister = sampleWithRequiredData;
        expectedResult = service.addAssetRegisterToCollectionIfMissing([], assetRegister);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRegister);
      });

      it('should not add a AssetRegister to an array that contains it', () => {
        const assetRegister: IAssetRegister = sampleWithRequiredData;
        const assetRegisterCollection: IAssetRegister[] = [
          {
            ...assetRegister,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAssetRegisterToCollectionIfMissing(assetRegisterCollection, assetRegister);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetRegister to an array that doesn't contain it", () => {
        const assetRegister: IAssetRegister = sampleWithRequiredData;
        const assetRegisterCollection: IAssetRegister[] = [sampleWithPartialData];
        expectedResult = service.addAssetRegisterToCollectionIfMissing(assetRegisterCollection, assetRegister);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRegister);
      });

      it('should add only unique AssetRegister to an array', () => {
        const assetRegisterArray: IAssetRegister[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const assetRegisterCollection: IAssetRegister[] = [sampleWithRequiredData];
        expectedResult = service.addAssetRegisterToCollectionIfMissing(assetRegisterCollection, ...assetRegisterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetRegister: IAssetRegister = sampleWithRequiredData;
        const assetRegister2: IAssetRegister = sampleWithPartialData;
        expectedResult = service.addAssetRegisterToCollectionIfMissing([], assetRegister, assetRegister2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRegister);
        expect(expectedResult).toContain(assetRegister2);
      });

      it('should accept null and undefined values', () => {
        const assetRegister: IAssetRegister = sampleWithRequiredData;
        expectedResult = service.addAssetRegisterToCollectionIfMissing([], null, assetRegister, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRegister);
      });

      it('should return initial array if no AssetRegister is added', () => {
        const assetRegisterCollection: IAssetRegister[] = [sampleWithRequiredData];
        expectedResult = service.addAssetRegisterToCollectionIfMissing(assetRegisterCollection, undefined, null);
        expect(expectedResult).toEqual(assetRegisterCollection);
      });
    });

    describe('compareAssetRegister', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAssetRegister(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25580 };
        const entity2 = null;

        const compareResult1 = service.compareAssetRegister(entity1, entity2);
        const compareResult2 = service.compareAssetRegister(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25580 };
        const entity2 = { id: 5579 };

        const compareResult1 = service.compareAssetRegister(entity1, entity2);
        const compareResult2 = service.compareAssetRegister(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25580 };
        const entity2 = { id: 25580 };

        const compareResult1 = service.compareAssetRegister(entity1, entity2);
        const compareResult2 = service.compareAssetRegister(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
