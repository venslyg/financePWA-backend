import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PettyCashLedgerService } from '../service/petty-cash-ledger.service';
import { IPettyCashLedger } from '../petty-cash-ledger.model';
import { PettyCashLedgerFormService } from './petty-cash-ledger-form.service';

import { PettyCashLedgerUpdateComponent } from './petty-cash-ledger-update.component';

describe('PettyCashLedger Management Update Component', () => {
  let comp: PettyCashLedgerUpdateComponent;
  let fixture: ComponentFixture<PettyCashLedgerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pettyCashLedgerFormService: PettyCashLedgerFormService;
  let pettyCashLedgerService: PettyCashLedgerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PettyCashLedgerUpdateComponent],
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
      .overrideTemplate(PettyCashLedgerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PettyCashLedgerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pettyCashLedgerFormService = TestBed.inject(PettyCashLedgerFormService);
    pettyCashLedgerService = TestBed.inject(PettyCashLedgerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const pettyCashLedger: IPettyCashLedger = { id: 27438 };

      activatedRoute.data = of({ pettyCashLedger });
      comp.ngOnInit();

      expect(comp.pettyCashLedger).toEqual(pettyCashLedger);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPettyCashLedger>>();
      const pettyCashLedger = { id: 23503 };
      jest.spyOn(pettyCashLedgerFormService, 'getPettyCashLedger').mockReturnValue(pettyCashLedger);
      jest.spyOn(pettyCashLedgerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pettyCashLedger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pettyCashLedger }));
      saveSubject.complete();

      // THEN
      expect(pettyCashLedgerFormService.getPettyCashLedger).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pettyCashLedgerService.update).toHaveBeenCalledWith(expect.objectContaining(pettyCashLedger));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPettyCashLedger>>();
      const pettyCashLedger = { id: 23503 };
      jest.spyOn(pettyCashLedgerFormService, 'getPettyCashLedger').mockReturnValue({ id: null });
      jest.spyOn(pettyCashLedgerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pettyCashLedger: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pettyCashLedger }));
      saveSubject.complete();

      // THEN
      expect(pettyCashLedgerFormService.getPettyCashLedger).toHaveBeenCalled();
      expect(pettyCashLedgerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPettyCashLedger>>();
      const pettyCashLedger = { id: 23503 };
      jest.spyOn(pettyCashLedgerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pettyCashLedger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pettyCashLedgerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
