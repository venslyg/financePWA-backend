import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISalaryPayout } from '../salary-payout.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../salary-payout.test-samples';

import { RestSalaryPayout, SalaryPayoutService } from './salary-payout.service';

const requireRestSample: RestSalaryPayout = {
  ...sampleWithRequiredData,
  payoutDate: sampleWithRequiredData.payoutDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('SalaryPayout Service', () => {
  let service: SalaryPayoutService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalaryPayout | ISalaryPayout[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryPayoutService);
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

    it('should create a SalaryPayout', () => {
      const salaryPayout = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salaryPayout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalaryPayout', () => {
      const salaryPayout = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salaryPayout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalaryPayout', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalaryPayout', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalaryPayout', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SalaryPayout', () => {
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

    describe('addSalaryPayoutToCollectionIfMissing', () => {
      it('should add a SalaryPayout to an empty array', () => {
        const salaryPayout: ISalaryPayout = sampleWithRequiredData;
        expectedResult = service.addSalaryPayoutToCollectionIfMissing([], salaryPayout);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryPayout);
      });

      it('should not add a SalaryPayout to an array that contains it', () => {
        const salaryPayout: ISalaryPayout = sampleWithRequiredData;
        const salaryPayoutCollection: ISalaryPayout[] = [
          {
            ...salaryPayout,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryPayoutToCollectionIfMissing(salaryPayoutCollection, salaryPayout);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalaryPayout to an array that doesn't contain it", () => {
        const salaryPayout: ISalaryPayout = sampleWithRequiredData;
        const salaryPayoutCollection: ISalaryPayout[] = [sampleWithPartialData];
        expectedResult = service.addSalaryPayoutToCollectionIfMissing(salaryPayoutCollection, salaryPayout);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryPayout);
      });

      it('should add only unique SalaryPayout to an array', () => {
        const salaryPayoutArray: ISalaryPayout[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryPayoutCollection: ISalaryPayout[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryPayoutToCollectionIfMissing(salaryPayoutCollection, ...salaryPayoutArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salaryPayout: ISalaryPayout = sampleWithRequiredData;
        const salaryPayout2: ISalaryPayout = sampleWithPartialData;
        expectedResult = service.addSalaryPayoutToCollectionIfMissing([], salaryPayout, salaryPayout2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryPayout);
        expect(expectedResult).toContain(salaryPayout2);
      });

      it('should accept null and undefined values', () => {
        const salaryPayout: ISalaryPayout = sampleWithRequiredData;
        expectedResult = service.addSalaryPayoutToCollectionIfMissing([], null, salaryPayout, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryPayout);
      });

      it('should return initial array if no SalaryPayout is added', () => {
        const salaryPayoutCollection: ISalaryPayout[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryPayoutToCollectionIfMissing(salaryPayoutCollection, undefined, null);
        expect(expectedResult).toEqual(salaryPayoutCollection);
      });
    });

    describe('compareSalaryPayout', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalaryPayout(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24337 };
        const entity2 = null;

        const compareResult1 = service.compareSalaryPayout(entity1, entity2);
        const compareResult2 = service.compareSalaryPayout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24337 };
        const entity2 = { id: 26419 };

        const compareResult1 = service.compareSalaryPayout(entity1, entity2);
        const compareResult2 = service.compareSalaryPayout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24337 };
        const entity2 = { id: 24337 };

        const compareResult1 = service.compareSalaryPayout(entity1, entity2);
        const compareResult2 = service.compareSalaryPayout(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
