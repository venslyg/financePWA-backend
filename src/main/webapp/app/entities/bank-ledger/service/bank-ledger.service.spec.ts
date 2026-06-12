import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBankLedger } from '../bank-ledger.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bank-ledger.test-samples';

import { BankLedgerService, RestBankLedger } from './bank-ledger.service';

const requireRestSample: RestBankLedger = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BankLedger Service', () => {
  let service: BankLedgerService;
  let httpMock: HttpTestingController;
  let expectedResult: IBankLedger | IBankLedger[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BankLedgerService);
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

    it('should create a BankLedger', () => {
      const bankLedger = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bankLedger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankLedger', () => {
      const bankLedger = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bankLedger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankLedger', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankLedger', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BankLedger', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a BankLedger', () => {
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

    describe('addBankLedgerToCollectionIfMissing', () => {
      it('should add a BankLedger to an empty array', () => {
        const bankLedger: IBankLedger = sampleWithRequiredData;
        expectedResult = service.addBankLedgerToCollectionIfMissing([], bankLedger);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankLedger);
      });

      it('should not add a BankLedger to an array that contains it', () => {
        const bankLedger: IBankLedger = sampleWithRequiredData;
        const bankLedgerCollection: IBankLedger[] = [
          {
            ...bankLedger,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBankLedgerToCollectionIfMissing(bankLedgerCollection, bankLedger);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankLedger to an array that doesn't contain it", () => {
        const bankLedger: IBankLedger = sampleWithRequiredData;
        const bankLedgerCollection: IBankLedger[] = [sampleWithPartialData];
        expectedResult = service.addBankLedgerToCollectionIfMissing(bankLedgerCollection, bankLedger);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankLedger);
      });

      it('should add only unique BankLedger to an array', () => {
        const bankLedgerArray: IBankLedger[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bankLedgerCollection: IBankLedger[] = [sampleWithRequiredData];
        expectedResult = service.addBankLedgerToCollectionIfMissing(bankLedgerCollection, ...bankLedgerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankLedger: IBankLedger = sampleWithRequiredData;
        const bankLedger2: IBankLedger = sampleWithPartialData;
        expectedResult = service.addBankLedgerToCollectionIfMissing([], bankLedger, bankLedger2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankLedger);
        expect(expectedResult).toContain(bankLedger2);
      });

      it('should accept null and undefined values', () => {
        const bankLedger: IBankLedger = sampleWithRequiredData;
        expectedResult = service.addBankLedgerToCollectionIfMissing([], null, bankLedger, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankLedger);
      });

      it('should return initial array if no BankLedger is added', () => {
        const bankLedgerCollection: IBankLedger[] = [sampleWithRequiredData];
        expectedResult = service.addBankLedgerToCollectionIfMissing(bankLedgerCollection, undefined, null);
        expect(expectedResult).toEqual(bankLedgerCollection);
      });
    });

    describe('compareBankLedger', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBankLedger(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4046 };
        const entity2 = null;

        const compareResult1 = service.compareBankLedger(entity1, entity2);
        const compareResult2 = service.compareBankLedger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4046 };
        const entity2 = { id: 589 };

        const compareResult1 = service.compareBankLedger(entity1, entity2);
        const compareResult2 = service.compareBankLedger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4046 };
        const entity2 = { id: 4046 };

        const compareResult1 = service.compareBankLedger(entity1, entity2);
        const compareResult2 = service.compareBankLedger(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
