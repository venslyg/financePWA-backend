import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExpenseCategory } from 'app/entities/expense-category/expense-category.model';
import { ExpenseCategoryService } from 'app/entities/expense-category/service/expense-category.service';
import { ExpenseSubCategoryService } from '../service/expense-sub-category.service';
import { IExpenseSubCategory } from '../expense-sub-category.model';
import { ExpenseSubCategoryFormService } from './expense-sub-category-form.service';

import { ExpenseSubCategoryUpdateComponent } from './expense-sub-category-update.component';

describe('ExpenseSubCategory Management Update Component', () => {
  let comp: ExpenseSubCategoryUpdateComponent;
  let fixture: ComponentFixture<ExpenseSubCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseSubCategoryFormService: ExpenseSubCategoryFormService;
  let expenseSubCategoryService: ExpenseSubCategoryService;
  let expenseCategoryService: ExpenseCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExpenseSubCategoryUpdateComponent],
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
      .overrideTemplate(ExpenseSubCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseSubCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseSubCategoryFormService = TestBed.inject(ExpenseSubCategoryFormService);
    expenseSubCategoryService = TestBed.inject(ExpenseSubCategoryService);
    expenseCategoryService = TestBed.inject(ExpenseCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ExpenseCategory query and add missing value', () => {
      const expenseSubCategory: IExpenseSubCategory = { id: 15993 };
      const category: IExpenseCategory = { id: 17564 };
      expenseSubCategory.category = category;

      const expenseCategoryCollection: IExpenseCategory[] = [{ id: 17564 }];
      jest.spyOn(expenseCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseCategoryCollection })));
      const additionalExpenseCategories = [category];
      const expectedCollection: IExpenseCategory[] = [...additionalExpenseCategories, ...expenseCategoryCollection];
      jest.spyOn(expenseCategoryService, 'addExpenseCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseSubCategory });
      comp.ngOnInit();

      expect(expenseCategoryService.query).toHaveBeenCalled();
      expect(expenseCategoryService.addExpenseCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        expenseCategoryCollection,
        ...additionalExpenseCategories.map(expect.objectContaining),
      );
      expect(comp.expenseCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const expenseSubCategory: IExpenseSubCategory = { id: 15993 };
      const category: IExpenseCategory = { id: 17564 };
      expenseSubCategory.category = category;

      activatedRoute.data = of({ expenseSubCategory });
      comp.ngOnInit();

      expect(comp.expenseCategoriesSharedCollection).toContainEqual(category);
      expect(comp.expenseSubCategory).toEqual(expenseSubCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseSubCategory>>();
      const expenseSubCategory = { id: 23454 };
      jest.spyOn(expenseSubCategoryFormService, 'getExpenseSubCategory').mockReturnValue(expenseSubCategory);
      jest.spyOn(expenseSubCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseSubCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseSubCategory }));
      saveSubject.complete();

      // THEN
      expect(expenseSubCategoryFormService.getExpenseSubCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseSubCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(expenseSubCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseSubCategory>>();
      const expenseSubCategory = { id: 23454 };
      jest.spyOn(expenseSubCategoryFormService, 'getExpenseSubCategory').mockReturnValue({ id: null });
      jest.spyOn(expenseSubCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseSubCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseSubCategory }));
      saveSubject.complete();

      // THEN
      expect(expenseSubCategoryFormService.getExpenseSubCategory).toHaveBeenCalled();
      expect(expenseSubCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseSubCategory>>();
      const expenseSubCategory = { id: 23454 };
      jest.spyOn(expenseSubCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseSubCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseSubCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExpenseCategory', () => {
      it('should forward to expenseCategoryService', () => {
        const entity = { id: 17564 };
        const entity2 = { id: 28308 };
        jest.spyOn(expenseCategoryService, 'compareExpenseCategory');
        comp.compareExpenseCategory(entity, entity2);
        expect(expenseCategoryService.compareExpenseCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
