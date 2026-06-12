import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ChurchStaffService } from '../service/church-staff.service';
import { IChurchStaff } from '../church-staff.model';
import { ChurchStaffFormService } from './church-staff-form.service';

import { ChurchStaffUpdateComponent } from './church-staff-update.component';

describe('ChurchStaff Management Update Component', () => {
  let comp: ChurchStaffUpdateComponent;
  let fixture: ComponentFixture<ChurchStaffUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let churchStaffFormService: ChurchStaffFormService;
  let churchStaffService: ChurchStaffService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChurchStaffUpdateComponent],
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
      .overrideTemplate(ChurchStaffUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChurchStaffUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    churchStaffFormService = TestBed.inject(ChurchStaffFormService);
    churchStaffService = TestBed.inject(ChurchStaffService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const churchStaff: IChurchStaff = { id: 26103 };

      activatedRoute.data = of({ churchStaff });
      comp.ngOnInit();

      expect(comp.churchStaff).toEqual(churchStaff);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurchStaff>>();
      const churchStaff = { id: 18086 };
      jest.spyOn(churchStaffFormService, 'getChurchStaff').mockReturnValue(churchStaff);
      jest.spyOn(churchStaffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ churchStaff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: churchStaff }));
      saveSubject.complete();

      // THEN
      expect(churchStaffFormService.getChurchStaff).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(churchStaffService.update).toHaveBeenCalledWith(expect.objectContaining(churchStaff));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurchStaff>>();
      const churchStaff = { id: 18086 };
      jest.spyOn(churchStaffFormService, 'getChurchStaff').mockReturnValue({ id: null });
      jest.spyOn(churchStaffService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ churchStaff: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: churchStaff }));
      saveSubject.complete();

      // THEN
      expect(churchStaffFormService.getChurchStaff).toHaveBeenCalled();
      expect(churchStaffService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurchStaff>>();
      const churchStaff = { id: 18086 };
      jest.spyOn(churchStaffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ churchStaff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(churchStaffService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
