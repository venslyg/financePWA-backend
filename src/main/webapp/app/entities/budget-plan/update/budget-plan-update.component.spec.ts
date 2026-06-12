import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BudgetPlanService } from '../service/budget-plan.service';
import { IBudgetPlan } from '../budget-plan.model';
import { BudgetPlanFormService } from './budget-plan-form.service';

import { BudgetPlanUpdateComponent } from './budget-plan-update.component';

describe('BudgetPlan Management Update Component', () => {
  let comp: BudgetPlanUpdateComponent;
  let fixture: ComponentFixture<BudgetPlanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetPlanFormService: BudgetPlanFormService;
  let budgetPlanService: BudgetPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BudgetPlanUpdateComponent],
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
      .overrideTemplate(BudgetPlanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BudgetPlanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    budgetPlanFormService = TestBed.inject(BudgetPlanFormService);
    budgetPlanService = TestBed.inject(BudgetPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const budgetPlan: IBudgetPlan = { id: 20444 };

      activatedRoute.data = of({ budgetPlan });
      comp.ngOnInit();

      expect(comp.budgetPlan).toEqual(budgetPlan);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetPlan>>();
      const budgetPlan = { id: 6467 };
      jest.spyOn(budgetPlanFormService, 'getBudgetPlan').mockReturnValue(budgetPlan);
      jest.spyOn(budgetPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetPlan }));
      saveSubject.complete();

      // THEN
      expect(budgetPlanFormService.getBudgetPlan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(budgetPlanService.update).toHaveBeenCalledWith(expect.objectContaining(budgetPlan));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetPlan>>();
      const budgetPlan = { id: 6467 };
      jest.spyOn(budgetPlanFormService, 'getBudgetPlan').mockReturnValue({ id: null });
      jest.spyOn(budgetPlanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetPlan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetPlan }));
      saveSubject.complete();

      // THEN
      expect(budgetPlanFormService.getBudgetPlan).toHaveBeenCalled();
      expect(budgetPlanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetPlan>>();
      const budgetPlan = { id: 6467 };
      jest.spyOn(budgetPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(budgetPlanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
