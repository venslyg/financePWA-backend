import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BinCardLineService } from '../service/bin-card-line.service';
import { IBinCardLine } from '../bin-card-line.model';
import { BinCardLineFormService } from './bin-card-line-form.service';

import { BinCardLineUpdateComponent } from './bin-card-line-update.component';

describe('BinCardLine Management Update Component', () => {
  let comp: BinCardLineUpdateComponent;
  let fixture: ComponentFixture<BinCardLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let binCardLineFormService: BinCardLineFormService;
  let binCardLineService: BinCardLineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BinCardLineUpdateComponent],
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
      .overrideTemplate(BinCardLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BinCardLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    binCardLineFormService = TestBed.inject(BinCardLineFormService);
    binCardLineService = TestBed.inject(BinCardLineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const binCardLine: IBinCardLine = { id: 4079 };

      activatedRoute.data = of({ binCardLine });
      comp.ngOnInit();

      expect(comp.binCardLine).toEqual(binCardLine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBinCardLine>>();
      const binCardLine = { id: 10587 };
      jest.spyOn(binCardLineFormService, 'getBinCardLine').mockReturnValue(binCardLine);
      jest.spyOn(binCardLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ binCardLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: binCardLine }));
      saveSubject.complete();

      // THEN
      expect(binCardLineFormService.getBinCardLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(binCardLineService.update).toHaveBeenCalledWith(expect.objectContaining(binCardLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBinCardLine>>();
      const binCardLine = { id: 10587 };
      jest.spyOn(binCardLineFormService, 'getBinCardLine').mockReturnValue({ id: null });
      jest.spyOn(binCardLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ binCardLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: binCardLine }));
      saveSubject.complete();

      // THEN
      expect(binCardLineFormService.getBinCardLine).toHaveBeenCalled();
      expect(binCardLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBinCardLine>>();
      const binCardLine = { id: 10587 };
      jest.spyOn(binCardLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ binCardLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(binCardLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
