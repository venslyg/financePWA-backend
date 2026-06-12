import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAssetRegister } from 'app/entities/asset-register/asset-register.model';
import { AssetRegisterService } from 'app/entities/asset-register/service/asset-register.service';
import { MaintenanceLogService } from '../service/maintenance-log.service';
import { IMaintenanceLog } from '../maintenance-log.model';
import { MaintenanceLogFormService } from './maintenance-log-form.service';

import { MaintenanceLogUpdateComponent } from './maintenance-log-update.component';

describe('MaintenanceLog Management Update Component', () => {
  let comp: MaintenanceLogUpdateComponent;
  let fixture: ComponentFixture<MaintenanceLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maintenanceLogFormService: MaintenanceLogFormService;
  let maintenanceLogService: MaintenanceLogService;
  let assetRegisterService: AssetRegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaintenanceLogUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MaintenanceLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaintenanceLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintenanceLogFormService = TestBed.inject(MaintenanceLogFormService);
    maintenanceLogService = TestBed.inject(MaintenanceLogService);
    assetRegisterService = TestBed.inject(AssetRegisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AssetRegister query and add missing value', () => {
      const maintenanceLog: IMaintenanceLog = { id: 20716 };
      const asset: IAssetRegister = { id: 25580 };
      maintenanceLog.asset = asset;

      const assetRegisterCollection: IAssetRegister[] = [{ id: 25580 }];
      jest.spyOn(assetRegisterService, 'query').mockReturnValue(of(new HttpResponse({ body: assetRegisterCollection })));
      const additionalAssetRegisters = [asset];
      const expectedCollection: IAssetRegister[] = [...additionalAssetRegisters, ...assetRegisterCollection];
      jest.spyOn(assetRegisterService, 'addAssetRegisterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintenanceLog });
      comp.ngOnInit();

      expect(assetRegisterService.query).toHaveBeenCalled();
      expect(assetRegisterService.addAssetRegisterToCollectionIfMissing).toHaveBeenCalledWith(
        assetRegisterCollection,
        ...additionalAssetRegisters.map(expect.objectContaining),
      );
      expect(comp.assetRegistersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const maintenanceLog: IMaintenanceLog = { id: 20716 };
      const asset: IAssetRegister = { id: 25580 };
      maintenanceLog.asset = asset;

      activatedRoute.data = of({ maintenanceLog });
      comp.ngOnInit();

      expect(comp.assetRegistersSharedCollection).toContainEqual(asset);
      expect(comp.maintenanceLog).toEqual(maintenanceLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceLog>>();
      const maintenanceLog = { id: 18508 };
      jest.spyOn(maintenanceLogFormService, 'getMaintenanceLog').mockReturnValue(maintenanceLog);
      jest.spyOn(maintenanceLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenanceLog }));
      saveSubject.complete();

      // THEN
      expect(maintenanceLogFormService.getMaintenanceLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintenanceLogService.update).toHaveBeenCalledWith(expect.objectContaining(maintenanceLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceLog>>();
      const maintenanceLog = { id: 18508 };
      jest.spyOn(maintenanceLogFormService, 'getMaintenanceLog').mockReturnValue({ id: null });
      jest.spyOn(maintenanceLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenanceLog }));
      saveSubject.complete();

      // THEN
      expect(maintenanceLogFormService.getMaintenanceLog).toHaveBeenCalled();
      expect(maintenanceLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceLog>>();
      const maintenanceLog = { id: 18508 };
      jest.spyOn(maintenanceLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintenanceLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssetRegister', () => {
      it('should forward to assetRegisterService', () => {
        const entity = { id: 25580 };
        const entity2 = { id: 5579 };
        jest.spyOn(assetRegisterService, 'compareAssetRegister');
        comp.compareAssetRegister(entity, entity2);
        expect(assetRegisterService.compareAssetRegister).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
