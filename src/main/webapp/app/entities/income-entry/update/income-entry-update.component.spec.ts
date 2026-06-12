import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IncomeEntryService } from '../service/income-entry.service';
import { IIncomeEntry } from '../income-entry.model';
import { IncomeEntryFormService } from './income-entry-form.service';

import { IncomeEntryUpdateComponent } from './income-entry-update.component';

describe('IncomeEntry Management Update Component', () => {
  let comp: IncomeEntryUpdateComponent;
  let fixture: ComponentFixture<IncomeEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomeEntryFormService: IncomeEntryFormService;
  let incomeEntryService: IncomeEntryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IncomeEntryUpdateComponent],
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
      .overrideTemplate(IncomeEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomeEntryFormService = TestBed.inject(IncomeEntryFormService);
    incomeEntryService = TestBed.inject(IncomeEntryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const incomeEntry: IIncomeEntry = { id: 29686 };

      activatedRoute.data = of({ incomeEntry });
      comp.ngOnInit();

      expect(comp.incomeEntry).toEqual(incomeEntry);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeEntry>>();
      const incomeEntry = { id: 26683 };
      jest.spyOn(incomeEntryFormService, 'getIncomeEntry').mockReturnValue(incomeEntry);
      jest.spyOn(incomeEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeEntry }));
      saveSubject.complete();

      // THEN
      expect(incomeEntryFormService.getIncomeEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomeEntryService.update).toHaveBeenCalledWith(expect.objectContaining(incomeEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeEntry>>();
      const incomeEntry = { id: 26683 };
      jest.spyOn(incomeEntryFormService, 'getIncomeEntry').mockReturnValue({ id: null });
      jest.spyOn(incomeEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeEntry }));
      saveSubject.complete();

      // THEN
      expect(incomeEntryFormService.getIncomeEntry).toHaveBeenCalled();
      expect(incomeEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeEntry>>();
      const incomeEntry = { id: 26683 };
      jest.spyOn(incomeEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomeEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
