import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExpenseEntry } from '../expense-entry.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../expense-entry.test-samples';

import { ExpenseEntryService, RestExpenseEntry } from './expense-entry.service';

const requireRestSample: RestExpenseEntry = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ExpenseEntry Service', () => {
  let service: ExpenseEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseEntry | IExpenseEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseEntryService);
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

    it('should create a ExpenseEntry', () => {
      const expenseEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseEntry', () => {
      const expenseEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ExpenseEntry', () => {
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

    describe('addExpenseEntryToCollectionIfMissing', () => {
      it('should add a ExpenseEntry to an empty array', () => {
        const expenseEntry: IExpenseEntry = sampleWithRequiredData;
        expectedResult = service.addExpenseEntryToCollectionIfMissing([], expenseEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseEntry);
      });

      it('should not add a ExpenseEntry to an array that contains it', () => {
        const expenseEntry: IExpenseEntry = sampleWithRequiredData;
        const expenseEntryCollection: IExpenseEntry[] = [
          {
            ...expenseEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseEntryToCollectionIfMissing(expenseEntryCollection, expenseEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseEntry to an array that doesn't contain it", () => {
        const expenseEntry: IExpenseEntry = sampleWithRequiredData;
        const expenseEntryCollection: IExpenseEntry[] = [sampleWithPartialData];
        expectedResult = service.addExpenseEntryToCollectionIfMissing(expenseEntryCollection, expenseEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseEntry);
      });

      it('should add only unique ExpenseEntry to an array', () => {
        const expenseEntryArray: IExpenseEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseEntryCollection: IExpenseEntry[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseEntryToCollectionIfMissing(expenseEntryCollection, ...expenseEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseEntry: IExpenseEntry = sampleWithRequiredData;
        const expenseEntry2: IExpenseEntry = sampleWithPartialData;
        expectedResult = service.addExpenseEntryToCollectionIfMissing([], expenseEntry, expenseEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseEntry);
        expect(expectedResult).toContain(expenseEntry2);
      });

      it('should accept null and undefined values', () => {
        const expenseEntry: IExpenseEntry = sampleWithRequiredData;
        expectedResult = service.addExpenseEntryToCollectionIfMissing([], null, expenseEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseEntry);
      });

      it('should return initial array if no ExpenseEntry is added', () => {
        const expenseEntryCollection: IExpenseEntry[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseEntryToCollectionIfMissing(expenseEntryCollection, undefined, null);
        expect(expectedResult).toEqual(expenseEntryCollection);
      });
    });

    describe('compareExpenseEntry', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1478 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseEntry(entity1, entity2);
        const compareResult2 = service.compareExpenseEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1478 };
        const entity2 = { id: 11100 };

        const compareResult1 = service.compareExpenseEntry(entity1, entity2);
        const compareResult2 = service.compareExpenseEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1478 };
        const entity2 = { id: 1478 };

        const compareResult1 = service.compareExpenseEntry(entity1, entity2);
        const compareResult2 = service.compareExpenseEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
