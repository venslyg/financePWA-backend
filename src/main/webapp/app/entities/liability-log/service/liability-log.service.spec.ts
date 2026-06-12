import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILiabilityLog } from '../liability-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../liability-log.test-samples';

import { LiabilityLogService, RestLiabilityLog } from './liability-log.service';

const requireRestSample: RestLiabilityLog = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('LiabilityLog Service', () => {
  let service: LiabilityLogService;
  let httpMock: HttpTestingController;
  let expectedResult: ILiabilityLog | ILiabilityLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LiabilityLogService);
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

    it('should create a LiabilityLog', () => {
      const liabilityLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(liabilityLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LiabilityLog', () => {
      const liabilityLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(liabilityLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LiabilityLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LiabilityLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LiabilityLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a LiabilityLog', () => {
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

    describe('addLiabilityLogToCollectionIfMissing', () => {
      it('should add a LiabilityLog to an empty array', () => {
        const liabilityLog: ILiabilityLog = sampleWithRequiredData;
        expectedResult = service.addLiabilityLogToCollectionIfMissing([], liabilityLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(liabilityLog);
      });

      it('should not add a LiabilityLog to an array that contains it', () => {
        const liabilityLog: ILiabilityLog = sampleWithRequiredData;
        const liabilityLogCollection: ILiabilityLog[] = [
          {
            ...liabilityLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLiabilityLogToCollectionIfMissing(liabilityLogCollection, liabilityLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LiabilityLog to an array that doesn't contain it", () => {
        const liabilityLog: ILiabilityLog = sampleWithRequiredData;
        const liabilityLogCollection: ILiabilityLog[] = [sampleWithPartialData];
        expectedResult = service.addLiabilityLogToCollectionIfMissing(liabilityLogCollection, liabilityLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(liabilityLog);
      });

      it('should add only unique LiabilityLog to an array', () => {
        const liabilityLogArray: ILiabilityLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const liabilityLogCollection: ILiabilityLog[] = [sampleWithRequiredData];
        expectedResult = service.addLiabilityLogToCollectionIfMissing(liabilityLogCollection, ...liabilityLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const liabilityLog: ILiabilityLog = sampleWithRequiredData;
        const liabilityLog2: ILiabilityLog = sampleWithPartialData;
        expectedResult = service.addLiabilityLogToCollectionIfMissing([], liabilityLog, liabilityLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(liabilityLog);
        expect(expectedResult).toContain(liabilityLog2);
      });

      it('should accept null and undefined values', () => {
        const liabilityLog: ILiabilityLog = sampleWithRequiredData;
        expectedResult = service.addLiabilityLogToCollectionIfMissing([], null, liabilityLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(liabilityLog);
      });

      it('should return initial array if no LiabilityLog is added', () => {
        const liabilityLogCollection: ILiabilityLog[] = [sampleWithRequiredData];
        expectedResult = service.addLiabilityLogToCollectionIfMissing(liabilityLogCollection, undefined, null);
        expect(expectedResult).toEqual(liabilityLogCollection);
      });
    });

    describe('compareLiabilityLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLiabilityLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18609 };
        const entity2 = null;

        const compareResult1 = service.compareLiabilityLog(entity1, entity2);
        const compareResult2 = service.compareLiabilityLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18609 };
        const entity2 = { id: 452 };

        const compareResult1 = service.compareLiabilityLog(entity1, entity2);
        const compareResult2 = service.compareLiabilityLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18609 };
        const entity2 = { id: 18609 };

        const compareResult1 = service.compareLiabilityLog(entity1, entity2);
        const compareResult2 = service.compareLiabilityLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
