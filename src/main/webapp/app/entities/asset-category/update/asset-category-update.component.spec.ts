import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AssetCategoryService } from '../service/asset-category.service';
import { IAssetCategory } from '../asset-category.model';
import { AssetCategoryFormService } from './asset-category-form.service';

import { AssetCategoryUpdateComponent } from './asset-category-update.component';

describe('AssetCategory Management Update Component', () => {
  let comp: AssetCategoryUpdateComponent;
  let fixture: ComponentFixture<AssetCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetCategoryFormService: AssetCategoryFormService;
  let assetCategoryService: AssetCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssetCategoryUpdateComponent],
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
      .overrideTemplate(AssetCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetCategoryFormService = TestBed.inject(AssetCategoryFormService);
    assetCategoryService = TestBed.inject(AssetCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const assetCategory: IAssetCategory = { id: 25378 };

      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      expect(comp.assetCategory).toEqual(assetCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCategory>>();
      const assetCategory = { id: 8038 };
      jest.spyOn(assetCategoryFormService, 'getAssetCategory').mockReturnValue(assetCategory);
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(assetCategoryFormService.getAssetCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(assetCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCategory>>();
      const assetCategory = { id: 8038 };
      jest.spyOn(assetCategoryFormService, 'getAssetCategory').mockReturnValue({ id: null });
      jest.spyOn(assetCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(assetCategoryFormService.getAssetCategory).toHaveBeenCalled();
      expect(assetCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCategory>>();
      const assetCategory = { id: 8038 };
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
