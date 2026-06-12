import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { LiabilityLogService } from '../service/liability-log.service';
import { ILiabilityLog } from '../liability-log.model';
import { LiabilityLogFormService } from './liability-log-form.service';

import { LiabilityLogUpdateComponent } from './liability-log-update.component';

describe('LiabilityLog Management Update Component', () => {
  let comp: LiabilityLogUpdateComponent;
  let fixture: ComponentFixture<LiabilityLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let liabilityLogFormService: LiabilityLogFormService;
  let liabilityLogService: LiabilityLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LiabilityLogUpdateComponent],
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
      .overrideTemplate(LiabilityLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LiabilityLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    liabilityLogFormService = TestBed.inject(LiabilityLogFormService);
    liabilityLogService = TestBed.inject(LiabilityLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const liabilityLog: ILiabilityLog = { id: 452 };

      activatedRoute.data = of({ liabilityLog });
      comp.ngOnInit();

      expect(comp.liabilityLog).toEqual(liabilityLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILiabilityLog>>();
      const liabilityLog = { id: 18609 };
      jest.spyOn(liabilityLogFormService, 'getLiabilityLog').mockReturnValue(liabilityLog);
      jest.spyOn(liabilityLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liabilityLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: liabilityLog }));
      saveSubject.complete();

      // THEN
      expect(liabilityLogFormService.getLiabilityLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(liabilityLogService.update).toHaveBeenCalledWith(expect.objectContaining(liabilityLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILiabilityLog>>();
      const liabilityLog = { id: 18609 };
      jest.spyOn(liabilityLogFormService, 'getLiabilityLog').mockReturnValue({ id: null });
      jest.spyOn(liabilityLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liabilityLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: liabilityLog }));
      saveSubject.complete();

      // THEN
      expect(liabilityLogFormService.getLiabilityLog).toHaveBeenCalled();
      expect(liabilityLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILiabilityLog>>();
      const liabilityLog = { id: 18609 };
      jest.spyOn(liabilityLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liabilityLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(liabilityLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
