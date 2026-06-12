import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ExpenseEntryService } from '../service/expense-entry.service';
import { IExpenseEntry } from '../expense-entry.model';
import { ExpenseEntryFormService } from './expense-entry-form.service';

import { ExpenseEntryUpdateComponent } from './expense-entry-update.component';

describe('ExpenseEntry Management Update Component', () => {
  let comp: ExpenseEntryUpdateComponent;
  let fixture: ComponentFixture<ExpenseEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseEntryFormService: ExpenseEntryFormService;
  let expenseEntryService: ExpenseEntryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExpenseEntryUpdateComponent],
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
      .overrideTemplate(ExpenseEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseEntryFormService = TestBed.inject(ExpenseEntryFormService);
    expenseEntryService = TestBed.inject(ExpenseEntryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const expenseEntry: IExpenseEntry = { id: 11100 };

      activatedRoute.data = of({ expenseEntry });
      comp.ngOnInit();

      expect(comp.expenseEntry).toEqual(expenseEntry);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseEntry>>();
      const expenseEntry = { id: 1478 };
      jest.spyOn(expenseEntryFormService, 'getExpenseEntry').mockReturnValue(expenseEntry);
      jest.spyOn(expenseEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseEntry }));
      saveSubject.complete();

      // THEN
      expect(expenseEntryFormService.getExpenseEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseEntryService.update).toHaveBeenCalledWith(expect.objectContaining(expenseEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseEntry>>();
      const expenseEntry = { id: 1478 };
      jest.spyOn(expenseEntryFormService, 'getExpenseEntry').mockReturnValue({ id: null });
      jest.spyOn(expenseEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseEntry }));
      saveSubject.complete();

      // THEN
      expect(expenseEntryFormService.getExpenseEntry).toHaveBeenCalled();
      expect(expenseEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseEntry>>();
      const expenseEntry = { id: 1478 };
      jest.spyOn(expenseEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
