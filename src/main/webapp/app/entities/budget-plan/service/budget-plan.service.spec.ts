import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBudgetPlan } from '../budget-plan.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../budget-plan.test-samples';

import { BudgetPlanService, RestBudgetPlan } from './budget-plan.service';

const requireRestSample: RestBudgetPlan = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BudgetPlan Service', () => {
  let service: BudgetPlanService;
  let httpMock: HttpTestingController;
  let expectedResult: IBudgetPlan | IBudgetPlan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BudgetPlanService);
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

    it('should create a BudgetPlan', () => {
      const budgetPlan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(budgetPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BudgetPlan', () => {
      const budgetPlan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(budgetPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BudgetPlan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BudgetPlan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BudgetPlan', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a BudgetPlan', () => {
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

    describe('addBudgetPlanToCollectionIfMissing', () => {
      it('should add a BudgetPlan to an empty array', () => {
        const budgetPlan: IBudgetPlan = sampleWithRequiredData;
        expectedResult = service.addBudgetPlanToCollectionIfMissing([], budgetPlan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetPlan);
      });

      it('should not add a BudgetPlan to an array that contains it', () => {
        const budgetPlan: IBudgetPlan = sampleWithRequiredData;
        const budgetPlanCollection: IBudgetPlan[] = [
          {
            ...budgetPlan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBudgetPlanToCollectionIfMissing(budgetPlanCollection, budgetPlan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BudgetPlan to an array that doesn't contain it", () => {
        const budgetPlan: IBudgetPlan = sampleWithRequiredData;
        const budgetPlanCollection: IBudgetPlan[] = [sampleWithPartialData];
        expectedResult = service.addBudgetPlanToCollectionIfMissing(budgetPlanCollection, budgetPlan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetPlan);
      });

      it('should add only unique BudgetPlan to an array', () => {
        const budgetPlanArray: IBudgetPlan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const budgetPlanCollection: IBudgetPlan[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetPlanToCollectionIfMissing(budgetPlanCollection, ...budgetPlanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const budgetPlan: IBudgetPlan = sampleWithRequiredData;
        const budgetPlan2: IBudgetPlan = sampleWithPartialData;
        expectedResult = service.addBudgetPlanToCollectionIfMissing([], budgetPlan, budgetPlan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetPlan);
        expect(expectedResult).toContain(budgetPlan2);
      });

      it('should accept null and undefined values', () => {
        const budgetPlan: IBudgetPlan = sampleWithRequiredData;
        expectedResult = service.addBudgetPlanToCollectionIfMissing([], null, budgetPlan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetPlan);
      });

      it('should return initial array if no BudgetPlan is added', () => {
        const budgetPlanCollection: IBudgetPlan[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetPlanToCollectionIfMissing(budgetPlanCollection, undefined, null);
        expect(expectedResult).toEqual(budgetPlanCollection);
      });
    });

    describe('compareBudgetPlan', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBudgetPlan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6467 };
        const entity2 = null;

        const compareResult1 = service.compareBudgetPlan(entity1, entity2);
        const compareResult2 = service.compareBudgetPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6467 };
        const entity2 = { id: 20444 };

        const compareResult1 = service.compareBudgetPlan(entity1, entity2);
        const compareResult2 = service.compareBudgetPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6467 };
        const entity2 = { id: 6467 };

        const compareResult1 = service.compareBudgetPlan(entity1, entity2);
        const compareResult2 = service.compareBudgetPlan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
