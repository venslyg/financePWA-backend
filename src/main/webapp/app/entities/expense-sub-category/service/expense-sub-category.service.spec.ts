import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExpenseSubCategory } from '../expense-sub-category.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../expense-sub-category.test-samples';

import { ExpenseSubCategoryService, RestExpenseSubCategory } from './expense-sub-category.service';

const requireRestSample: RestExpenseSubCategory = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ExpenseSubCategory Service', () => {
  let service: ExpenseSubCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseSubCategory | IExpenseSubCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseSubCategoryService);
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

    it('should create a ExpenseSubCategory', () => {
      const expenseSubCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseSubCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseSubCategory', () => {
      const expenseSubCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseSubCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseSubCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseSubCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseSubCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ExpenseSubCategory', () => {
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

    describe('addExpenseSubCategoryToCollectionIfMissing', () => {
      it('should add a ExpenseSubCategory to an empty array', () => {
        const expenseSubCategory: IExpenseSubCategory = sampleWithRequiredData;
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing([], expenseSubCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseSubCategory);
      });

      it('should not add a ExpenseSubCategory to an array that contains it', () => {
        const expenseSubCategory: IExpenseSubCategory = sampleWithRequiredData;
        const expenseSubCategoryCollection: IExpenseSubCategory[] = [
          {
            ...expenseSubCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing(expenseSubCategoryCollection, expenseSubCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseSubCategory to an array that doesn't contain it", () => {
        const expenseSubCategory: IExpenseSubCategory = sampleWithRequiredData;
        const expenseSubCategoryCollection: IExpenseSubCategory[] = [sampleWithPartialData];
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing(expenseSubCategoryCollection, expenseSubCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseSubCategory);
      });

      it('should add only unique ExpenseSubCategory to an array', () => {
        const expenseSubCategoryArray: IExpenseSubCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseSubCategoryCollection: IExpenseSubCategory[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing(expenseSubCategoryCollection, ...expenseSubCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseSubCategory: IExpenseSubCategory = sampleWithRequiredData;
        const expenseSubCategory2: IExpenseSubCategory = sampleWithPartialData;
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing([], expenseSubCategory, expenseSubCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseSubCategory);
        expect(expectedResult).toContain(expenseSubCategory2);
      });

      it('should accept null and undefined values', () => {
        const expenseSubCategory: IExpenseSubCategory = sampleWithRequiredData;
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing([], null, expenseSubCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseSubCategory);
      });

      it('should return initial array if no ExpenseSubCategory is added', () => {
        const expenseSubCategoryCollection: IExpenseSubCategory[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseSubCategoryToCollectionIfMissing(expenseSubCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(expenseSubCategoryCollection);
      });
    });

    describe('compareExpenseSubCategory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseSubCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23454 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseSubCategory(entity1, entity2);
        const compareResult2 = service.compareExpenseSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23454 };
        const entity2 = { id: 15993 };

        const compareResult1 = service.compareExpenseSubCategory(entity1, entity2);
        const compareResult2 = service.compareExpenseSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23454 };
        const entity2 = { id: 23454 };

        const compareResult1 = service.compareExpenseSubCategory(entity1, entity2);
        const compareResult2 = service.compareExpenseSubCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
