import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AssetDepreciationHistoryService } from '../service/asset-depreciation-history.service';
import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';
import { AssetDepreciationHistoryFormService } from './asset-depreciation-history-form.service';

import { AssetDepreciationHistoryUpdateComponent } from './asset-depreciation-history-update.component';

describe('AssetDepreciationHistory Management Update Component', () => {
  let comp: AssetDepreciationHistoryUpdateComponent;
  let fixture: ComponentFixture<AssetDepreciationHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetDepreciationHistoryFormService: AssetDepreciationHistoryFormService;
  let assetDepreciationHistoryService: AssetDepreciationHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssetDepreciationHistoryUpdateComponent],
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
      .overrideTemplate(AssetDepreciationHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetDepreciationHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetDepreciationHistoryFormService = TestBed.inject(AssetDepreciationHistoryFormService);
    assetDepreciationHistoryService = TestBed.inject(AssetDepreciationHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const assetDepreciationHistory: IAssetDepreciationHistory = { id: 23096 };

      activatedRoute.data = of({ assetDepreciationHistory });
      comp.ngOnInit();

      expect(comp.assetDepreciationHistory).toEqual(assetDepreciationHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetDepreciationHistory>>();
      const assetDepreciationHistory = { id: 23291 };
      jest.spyOn(assetDepreciationHistoryFormService, 'getAssetDepreciationHistory').mockReturnValue(assetDepreciationHistory);
      jest.spyOn(assetDepreciationHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDepreciationHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDepreciationHistory }));
      saveSubject.complete();

      // THEN
      expect(assetDepreciationHistoryFormService.getAssetDepreciationHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetDepreciationHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(assetDepreciationHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetDepreciationHistory>>();
      const assetDepreciationHistory = { id: 23291 };
      jest.spyOn(assetDepreciationHistoryFormService, 'getAssetDepreciationHistory').mockReturnValue({ id: null });
      jest.spyOn(assetDepreciationHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDepreciationHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDepreciationHistory }));
      saveSubject.complete();

      // THEN
      expect(assetDepreciationHistoryFormService.getAssetDepreciationHistory).toHaveBeenCalled();
      expect(assetDepreciationHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetDepreciationHistory>>();
      const assetDepreciationHistory = { id: 23291 };
      jest.spyOn(assetDepreciationHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDepreciationHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetDepreciationHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
