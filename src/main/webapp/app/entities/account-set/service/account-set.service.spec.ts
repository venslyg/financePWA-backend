import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAccountSet } from '../account-set.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../account-set.test-samples';

import { AccountSetService, RestAccountSet } from './account-set.service';

const requireRestSample: RestAccountSet = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('AccountSet Service', () => {
  let service: AccountSetService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountSet | IAccountSet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AccountSetService);
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

    it('should create a AccountSet', () => {
      const accountSet = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountSet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountSet', () => {
      const accountSet = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountSet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountSet', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountSet', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountSet', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccountSet', () => {
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

    describe('addAccountSetToCollectionIfMissing', () => {
      it('should add a AccountSet to an empty array', () => {
        const accountSet: IAccountSet = sampleWithRequiredData;
        expectedResult = service.addAccountSetToCollectionIfMissing([], accountSet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountSet);
      });

      it('should not add a AccountSet to an array that contains it', () => {
        const accountSet: IAccountSet = sampleWithRequiredData;
        const accountSetCollection: IAccountSet[] = [
          {
            ...accountSet,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountSetToCollectionIfMissing(accountSetCollection, accountSet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountSet to an array that doesn't contain it", () => {
        const accountSet: IAccountSet = sampleWithRequiredData;
        const accountSetCollection: IAccountSet[] = [sampleWithPartialData];
        expectedResult = service.addAccountSetToCollectionIfMissing(accountSetCollection, accountSet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountSet);
      });

      it('should add only unique AccountSet to an array', () => {
        const accountSetArray: IAccountSet[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accountSetCollection: IAccountSet[] = [sampleWithRequiredData];
        expectedResult = service.addAccountSetToCollectionIfMissing(accountSetCollection, ...accountSetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountSet: IAccountSet = sampleWithRequiredData;
        const accountSet2: IAccountSet = sampleWithPartialData;
        expectedResult = service.addAccountSetToCollectionIfMissing([], accountSet, accountSet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountSet);
        expect(expectedResult).toContain(accountSet2);
      });

      it('should accept null and undefined values', () => {
        const accountSet: IAccountSet = sampleWithRequiredData;
        expectedResult = service.addAccountSetToCollectionIfMissing([], null, accountSet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountSet);
      });

      it('should return initial array if no AccountSet is added', () => {
        const accountSetCollection: IAccountSet[] = [sampleWithRequiredData];
        expectedResult = service.addAccountSetToCollectionIfMissing(accountSetCollection, undefined, null);
        expect(expectedResult).toEqual(accountSetCollection);
      });
    });

    describe('compareAccountSet', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountSet(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9279 };
        const entity2 = null;

        const compareResult1 = service.compareAccountSet(entity1, entity2);
        const compareResult2 = service.compareAccountSet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9279 };
        const entity2 = { id: 25063 };

        const compareResult1 = service.compareAccountSet(entity1, entity2);
        const compareResult2 = service.compareAccountSet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9279 };
        const entity2 = { id: 9279 };

        const compareResult1 = service.compareAccountSet(entity1, entity2);
        const compareResult2 = service.compareAccountSet(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
