import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SalaryPayoutService } from '../service/salary-payout.service';
import { ISalaryPayout } from '../salary-payout.model';
import { SalaryPayoutFormService } from './salary-payout-form.service';

import { SalaryPayoutUpdateComponent } from './salary-payout-update.component';

describe('SalaryPayout Management Update Component', () => {
  let comp: SalaryPayoutUpdateComponent;
  let fixture: ComponentFixture<SalaryPayoutUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryPayoutFormService: SalaryPayoutFormService;
  let salaryPayoutService: SalaryPayoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SalaryPayoutUpdateComponent],
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
      .overrideTemplate(SalaryPayoutUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryPayoutUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryPayoutFormService = TestBed.inject(SalaryPayoutFormService);
    salaryPayoutService = TestBed.inject(SalaryPayoutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const salaryPayout: ISalaryPayout = { id: 26419 };

      activatedRoute.data = of({ salaryPayout });
      comp.ngOnInit();

      expect(comp.salaryPayout).toEqual(salaryPayout);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryPayout>>();
      const salaryPayout = { id: 24337 };
      jest.spyOn(salaryPayoutFormService, 'getSalaryPayout').mockReturnValue(salaryPayout);
      jest.spyOn(salaryPayoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryPayout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryPayout }));
      saveSubject.complete();

      // THEN
      expect(salaryPayoutFormService.getSalaryPayout).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryPayoutService.update).toHaveBeenCalledWith(expect.objectContaining(salaryPayout));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryPayout>>();
      const salaryPayout = { id: 24337 };
      jest.spyOn(salaryPayoutFormService, 'getSalaryPayout').mockReturnValue({ id: null });
      jest.spyOn(salaryPayoutService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryPayout: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryPayout }));
      saveSubject.complete();

      // THEN
      expect(salaryPayoutFormService.getSalaryPayout).toHaveBeenCalled();
      expect(salaryPayoutService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryPayout>>();
      const salaryPayout = { id: 24337 };
      jest.spyOn(salaryPayoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryPayout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryPayoutService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
