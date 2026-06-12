import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPettyCashLedger } from '../petty-cash-ledger.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../petty-cash-ledger.test-samples';

import { PettyCashLedgerService, RestPettyCashLedger } from './petty-cash-ledger.service';

const requireRestSample: RestPettyCashLedger = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('PettyCashLedger Service', () => {
  let service: PettyCashLedgerService;
  let httpMock: HttpTestingController;
  let expectedResult: IPettyCashLedger | IPettyCashLedger[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PettyCashLedgerService);
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

    it('should create a PettyCashLedger', () => {
      const pettyCashLedger = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pettyCashLedger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PettyCashLedger', () => {
      const pettyCashLedger = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pettyCashLedger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PettyCashLedger', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PettyCashLedger', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PettyCashLedger', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a PettyCashLedger', () => {
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

    describe('addPettyCashLedgerToCollectionIfMissing', () => {
      it('should add a PettyCashLedger to an empty array', () => {
        const pettyCashLedger: IPettyCashLedger = sampleWithRequiredData;
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing([], pettyCashLedger);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pettyCashLedger);
      });

      it('should not add a PettyCashLedger to an array that contains it', () => {
        const pettyCashLedger: IPettyCashLedger = sampleWithRequiredData;
        const pettyCashLedgerCollection: IPettyCashLedger[] = [
          {
            ...pettyCashLedger,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing(pettyCashLedgerCollection, pettyCashLedger);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PettyCashLedger to an array that doesn't contain it", () => {
        const pettyCashLedger: IPettyCashLedger = sampleWithRequiredData;
        const pettyCashLedgerCollection: IPettyCashLedger[] = [sampleWithPartialData];
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing(pettyCashLedgerCollection, pettyCashLedger);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pettyCashLedger);
      });

      it('should add only unique PettyCashLedger to an array', () => {
        const pettyCashLedgerArray: IPettyCashLedger[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pettyCashLedgerCollection: IPettyCashLedger[] = [sampleWithRequiredData];
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing(pettyCashLedgerCollection, ...pettyCashLedgerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pettyCashLedger: IPettyCashLedger = sampleWithRequiredData;
        const pettyCashLedger2: IPettyCashLedger = sampleWithPartialData;
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing([], pettyCashLedger, pettyCashLedger2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pettyCashLedger);
        expect(expectedResult).toContain(pettyCashLedger2);
      });

      it('should accept null and undefined values', () => {
        const pettyCashLedger: IPettyCashLedger = sampleWithRequiredData;
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing([], null, pettyCashLedger, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pettyCashLedger);
      });

      it('should return initial array if no PettyCashLedger is added', () => {
        const pettyCashLedgerCollection: IPettyCashLedger[] = [sampleWithRequiredData];
        expectedResult = service.addPettyCashLedgerToCollectionIfMissing(pettyCashLedgerCollection, undefined, null);
        expect(expectedResult).toEqual(pettyCashLedgerCollection);
      });
    });

    describe('comparePettyCashLedger', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePettyCashLedger(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23503 };
        const entity2 = null;

        const compareResult1 = service.comparePettyCashLedger(entity1, entity2);
        const compareResult2 = service.comparePettyCashLedger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23503 };
        const entity2 = { id: 27438 };

        const compareResult1 = service.comparePettyCashLedger(entity1, entity2);
        const compareResult2 = service.comparePettyCashLedger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23503 };
        const entity2 = { id: 23503 };

        const compareResult1 = service.comparePettyCashLedger(entity1, entity2);
        const compareResult2 = service.comparePettyCashLedger(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
