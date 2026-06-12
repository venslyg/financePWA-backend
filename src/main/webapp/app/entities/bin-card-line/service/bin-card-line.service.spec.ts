import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBinCardLine } from '../bin-card-line.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bin-card-line.test-samples';

import { BinCardLineService, RestBinCardLine } from './bin-card-line.service';

const requireRestSample: RestBinCardLine = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BinCardLine Service', () => {
  let service: BinCardLineService;
  let httpMock: HttpTestingController;
  let expectedResult: IBinCardLine | IBinCardLine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BinCardLineService);
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

    it('should create a BinCardLine', () => {
      const binCardLine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(binCardLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BinCardLine', () => {
      const binCardLine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(binCardLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BinCardLine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BinCardLine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BinCardLine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a BinCardLine', () => {
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

    describe('addBinCardLineToCollectionIfMissing', () => {
      it('should add a BinCardLine to an empty array', () => {
        const binCardLine: IBinCardLine = sampleWithRequiredData;
        expectedResult = service.addBinCardLineToCollectionIfMissing([], binCardLine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(binCardLine);
      });

      it('should not add a BinCardLine to an array that contains it', () => {
        const binCardLine: IBinCardLine = sampleWithRequiredData;
        const binCardLineCollection: IBinCardLine[] = [
          {
            ...binCardLine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBinCardLineToCollectionIfMissing(binCardLineCollection, binCardLine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BinCardLine to an array that doesn't contain it", () => {
        const binCardLine: IBinCardLine = sampleWithRequiredData;
        const binCardLineCollection: IBinCardLine[] = [sampleWithPartialData];
        expectedResult = service.addBinCardLineToCollectionIfMissing(binCardLineCollection, binCardLine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(binCardLine);
      });

      it('should add only unique BinCardLine to an array', () => {
        const binCardLineArray: IBinCardLine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const binCardLineCollection: IBinCardLine[] = [sampleWithRequiredData];
        expectedResult = service.addBinCardLineToCollectionIfMissing(binCardLineCollection, ...binCardLineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const binCardLine: IBinCardLine = sampleWithRequiredData;
        const binCardLine2: IBinCardLine = sampleWithPartialData;
        expectedResult = service.addBinCardLineToCollectionIfMissing([], binCardLine, binCardLine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(binCardLine);
        expect(expectedResult).toContain(binCardLine2);
      });

      it('should accept null and undefined values', () => {
        const binCardLine: IBinCardLine = sampleWithRequiredData;
        expectedResult = service.addBinCardLineToCollectionIfMissing([], null, binCardLine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(binCardLine);
      });

      it('should return initial array if no BinCardLine is added', () => {
        const binCardLineCollection: IBinCardLine[] = [sampleWithRequiredData];
        expectedResult = service.addBinCardLineToCollectionIfMissing(binCardLineCollection, undefined, null);
        expect(expectedResult).toEqual(binCardLineCollection);
      });
    });

    describe('compareBinCardLine', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBinCardLine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10587 };
        const entity2 = null;

        const compareResult1 = service.compareBinCardLine(entity1, entity2);
        const compareResult2 = service.compareBinCardLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10587 };
        const entity2 = { id: 4079 };

        const compareResult1 = service.compareBinCardLine(entity1, entity2);
        const compareResult2 = service.compareBinCardLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10587 };
        const entity2 = { id: 10587 };

        const compareResult1 = service.compareBinCardLine(entity1, entity2);
        const compareResult2 = service.compareBinCardLine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
