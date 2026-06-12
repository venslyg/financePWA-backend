import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAssetCategory } from 'app/entities/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/asset-category/service/asset-category.service';
import { AssetSubCategoryService } from '../service/asset-sub-category.service';
import { IAssetSubCategory } from '../asset-sub-category.model';
import { AssetSubCategoryFormService } from './asset-sub-category-form.service';

import { AssetSubCategoryUpdateComponent } from './asset-sub-category-update.component';

describe('AssetSubCategory Management Update Component', () => {
  let comp: AssetSubCategoryUpdateComponent;
  let fixture: ComponentFixture<AssetSubCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetSubCategoryFormService: AssetSubCategoryFormService;
  let assetSubCategoryService: AssetSubCategoryService;
  let assetCategoryService: AssetCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssetSubCategoryUpdateComponent],
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
      .overrideTemplate(AssetSubCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetSubCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetSubCategoryFormService = TestBed.inject(AssetSubCategoryFormService);
    assetSubCategoryService = TestBed.inject(AssetSubCategoryService);
    assetCategoryService = TestBed.inject(AssetCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AssetCategory query and add missing value', () => {
      const assetSubCategory: IAssetSubCategory = { id: 83 };
      const category: IAssetCategory = { id: 8038 };
      assetSubCategory.category = category;

      const assetCategoryCollection: IAssetCategory[] = [{ id: 8038 }];
      jest.spyOn(assetCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCategoryCollection })));
      const additionalAssetCategories = [category];
      const expectedCollection: IAssetCategory[] = [...additionalAssetCategories, ...assetCategoryCollection];
      jest.spyOn(assetCategoryService, 'addAssetCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetSubCategory });
      comp.ngOnInit();

      expect(assetCategoryService.query).toHaveBeenCalled();
      expect(assetCategoryService.addAssetCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        assetCategoryCollection,
        ...additionalAssetCategories.map(expect.objectContaining),
      );
      expect(comp.assetCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const assetSubCategory: IAssetSubCategory = { id: 83 };
      const category: IAssetCategory = { id: 8038 };
      assetSubCategory.category = category;

      activatedRoute.data = of({ assetSubCategory });
      comp.ngOnInit();

      expect(comp.assetCategoriesSharedCollection).toContainEqual(category);
      expect(comp.assetSubCategory).toEqual(assetSubCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetSubCategory>>();
      const assetSubCategory = { id: 24169 };
      jest.spyOn(assetSubCategoryFormService, 'getAssetSubCategory').mockReturnValue(assetSubCategory);
      jest.spyOn(assetSubCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetSubCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetSubCategory }));
      saveSubject.complete();

      // THEN
      expect(assetSubCategoryFormService.getAssetSubCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetSubCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(assetSubCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetSubCategory>>();
      const assetSubCategory = { id: 24169 };
      jest.spyOn(assetSubCategoryFormService, 'getAssetSubCategory').mockReturnValue({ id: null });
      jest.spyOn(assetSubCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetSubCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetSubCategory }));
      saveSubject.complete();

      // THEN
      expect(assetSubCategoryFormService.getAssetSubCategory).toHaveBeenCalled();
      expect(assetSubCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetSubCategory>>();
      const assetSubCategory = { id: 24169 };
      jest.spyOn(assetSubCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetSubCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetSubCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssetCategory', () => {
      it('should forward to assetCategoryService', () => {
        const entity = { id: 8038 };
        const entity2 = { id: 25378 };
        jest.spyOn(assetCategoryService, 'compareAssetCategory');
        comp.compareAssetCategory(entity, entity2);
        expect(assetCategoryService.compareAssetCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
