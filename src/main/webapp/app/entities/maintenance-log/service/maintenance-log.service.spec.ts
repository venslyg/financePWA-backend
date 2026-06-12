import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMaintenanceLog } from '../maintenance-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../maintenance-log.test-samples';

import { MaintenanceLogService, RestMaintenanceLog } from './maintenance-log.service';

const requireRestSample: RestMaintenanceLog = {
  ...sampleWithRequiredData,
  logDate: sampleWithRequiredData.logDate?.format(DATE_FORMAT),
  nextServiceDate: sampleWithRequiredData.nextServiceDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('MaintenanceLog Service', () => {
  let service: MaintenanceLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaintenanceLog | IMaintenanceLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaintenanceLogService);
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

    it('should create a MaintenanceLog', () => {
      const maintenanceLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maintenanceLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaintenanceLog', () => {
      const maintenanceLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maintenanceLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaintenanceLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaintenanceLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaintenanceLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MaintenanceLog', () => {
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

    describe('addMaintenanceLogToCollectionIfMissing', () => {
      it('should add a MaintenanceLog to an empty array', () => {
        const maintenanceLog: IMaintenanceLog = sampleWithRequiredData;
        expectedResult = service.addMaintenanceLogToCollectionIfMissing([], maintenanceLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintenanceLog);
      });

      it('should not add a MaintenanceLog to an array that contains it', () => {
        const maintenanceLog: IMaintenanceLog = sampleWithRequiredData;
        const maintenanceLogCollection: IMaintenanceLog[] = [
          {
            ...maintenanceLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaintenanceLogToCollectionIfMissing(maintenanceLogCollection, maintenanceLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaintenanceLog to an array that doesn't contain it", () => {
        const maintenanceLog: IMaintenanceLog = sampleWithRequiredData;
        const maintenanceLogCollection: IMaintenanceLog[] = [sampleWithPartialData];
        expectedResult = service.addMaintenanceLogToCollectionIfMissing(maintenanceLogCollection, maintenanceLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintenanceLog);
      });

      it('should add only unique MaintenanceLog to an array', () => {
        const maintenanceLogArray: IMaintenanceLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maintenanceLogCollection: IMaintenanceLog[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceLogToCollectionIfMissing(maintenanceLogCollection, ...maintenanceLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maintenanceLog: IMaintenanceLog = sampleWithRequiredData;
        const maintenanceLog2: IMaintenanceLog = sampleWithPartialData;
        expectedResult = service.addMaintenanceLogToCollectionIfMissing([], maintenanceLog, maintenanceLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintenanceLog);
        expect(expectedResult).toContain(maintenanceLog2);
      });

      it('should accept null and undefined values', () => {
        const maintenanceLog: IMaintenanceLog = sampleWithRequiredData;
        expectedResult = service.addMaintenanceLogToCollectionIfMissing([], null, maintenanceLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maintenanceLog);
      });

      it('should return initial array if no MaintenanceLog is added', () => {
        const maintenanceLogCollection: IMaintenanceLog[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceLogToCollectionIfMissing(maintenanceLogCollection, undefined, null);
        expect(expectedResult).toEqual(maintenanceLogCollection);
      });
    });

    describe('compareMaintenanceLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaintenanceLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18508 };
        const entity2 = null;

        const compareResult1 = service.compareMaintenanceLog(entity1, entity2);
        const compareResult2 = service.compareMaintenanceLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18508 };
        const entity2 = { id: 20716 };

        const compareResult1 = service.compareMaintenanceLog(entity1, entity2);
        const compareResult2 = service.compareMaintenanceLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18508 };
        const entity2 = { id: 18508 };

        const compareResult1 = service.compareMaintenanceLog(entity1, entity2);
        const compareResult2 = service.compareMaintenanceLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
