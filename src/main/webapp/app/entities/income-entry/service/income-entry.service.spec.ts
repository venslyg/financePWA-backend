import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIncomeEntry } from '../income-entry.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../income-entry.test-samples';

import { IncomeEntryService, RestIncomeEntry } from './income-entry.service';

const requireRestSample: RestIncomeEntry = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('IncomeEntry Service', () => {
  let service: IncomeEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncomeEntry | IIncomeEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IncomeEntryService);
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

    it('should create a IncomeEntry', () => {
      const incomeEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incomeEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncomeEntry', () => {
      const incomeEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incomeEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncomeEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncomeEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncomeEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a IncomeEntry', () => {
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

    describe('addIncomeEntryToCollectionIfMissing', () => {
      it('should add a IncomeEntry to an empty array', () => {
        const incomeEntry: IIncomeEntry = sampleWithRequiredData;
        expectedResult = service.addIncomeEntryToCollectionIfMissing([], incomeEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeEntry);
      });

      it('should not add a IncomeEntry to an array that contains it', () => {
        const incomeEntry: IIncomeEntry = sampleWithRequiredData;
        const incomeEntryCollection: IIncomeEntry[] = [
          {
            ...incomeEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncomeEntryToCollectionIfMissing(incomeEntryCollection, incomeEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncomeEntry to an array that doesn't contain it", () => {
        const incomeEntry: IIncomeEntry = sampleWithRequiredData;
        const incomeEntryCollection: IIncomeEntry[] = [sampleWithPartialData];
        expectedResult = service.addIncomeEntryToCollectionIfMissing(incomeEntryCollection, incomeEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeEntry);
      });

      it('should add only unique IncomeEntry to an array', () => {
        const incomeEntryArray: IIncomeEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incomeEntryCollection: IIncomeEntry[] = [sampleWithRequiredData];
        expectedResult = service.addIncomeEntryToCollectionIfMissing(incomeEntryCollection, ...incomeEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incomeEntry: IIncomeEntry = sampleWithRequiredData;
        const incomeEntry2: IIncomeEntry = sampleWithPartialData;
        expectedResult = service.addIncomeEntryToCollectionIfMissing([], incomeEntry, incomeEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incomeEntry);
        expect(expectedResult).toContain(incomeEntry2);
      });

      it('should accept null and undefined values', () => {
        const incomeEntry: IIncomeEntry = sampleWithRequiredData;
        expectedResult = service.addIncomeEntryToCollectionIfMissing([], null, incomeEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incomeEntry);
      });

      it('should return initial array if no IncomeEntry is added', () => {
        const incomeEntryCollection: IIncomeEntry[] = [sampleWithRequiredData];
        expectedResult = service.addIncomeEntryToCollectionIfMissing(incomeEntryCollection, undefined, null);
        expect(expectedResult).toEqual(incomeEntryCollection);
      });
    });

    describe('compareIncomeEntry', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncomeEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26683 };
        const entity2 = null;

        const compareResult1 = service.compareIncomeEntry(entity1, entity2);
        const compareResult2 = service.compareIncomeEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26683 };
        const entity2 = { id: 29686 };

        const compareResult1 = service.compareIncomeEntry(entity1, entity2);
        const compareResult2 = service.compareIncomeEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26683 };
        const entity2 = { id: 26683 };

        const compareResult1 = service.compareIncomeEntry(entity1, entity2);
        const compareResult2 = service.compareIncomeEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
