import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BankLedgerService } from '../service/bank-ledger.service';
import { IBankLedger } from '../bank-ledger.model';
import { BankLedgerFormService } from './bank-ledger-form.service';

import { BankLedgerUpdateComponent } from './bank-ledger-update.component';

describe('BankLedger Management Update Component', () => {
  let comp: BankLedgerUpdateComponent;
  let fixture: ComponentFixture<BankLedgerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankLedgerFormService: BankLedgerFormService;
  let bankLedgerService: BankLedgerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BankLedgerUpdateComponent],
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
      .overrideTemplate(BankLedgerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankLedgerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankLedgerFormService = TestBed.inject(BankLedgerFormService);
    bankLedgerService = TestBed.inject(BankLedgerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const bankLedger: IBankLedger = { id: 589 };

      activatedRoute.data = of({ bankLedger });
      comp.ngOnInit();

      expect(comp.bankLedger).toEqual(bankLedger);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankLedger>>();
      const bankLedger = { id: 4046 };
      jest.spyOn(bankLedgerFormService, 'getBankLedger').mockReturnValue(bankLedger);
      jest.spyOn(bankLedgerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankLedger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankLedger }));
      saveSubject.complete();

      // THEN
      expect(bankLedgerFormService.getBankLedger).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankLedgerService.update).toHaveBeenCalledWith(expect.objectContaining(bankLedger));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankLedger>>();
      const bankLedger = { id: 4046 };
      jest.spyOn(bankLedgerFormService, 'getBankLedger').mockReturnValue({ id: null });
      jest.spyOn(bankLedgerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankLedger: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bankLedger }));
      saveSubject.complete();

      // THEN
      expect(bankLedgerFormService.getBankLedger).toHaveBeenCalled();
      expect(bankLedgerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBankLedger>>();
      const bankLedger = { id: 4046 };
      jest.spyOn(bankLedgerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bankLedger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankLedgerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
