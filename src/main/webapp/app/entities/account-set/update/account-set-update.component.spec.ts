import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AccountSetService } from '../service/account-set.service';
import { IAccountSet } from '../account-set.model';
import { AccountSetFormService } from './account-set-form.service';

import { AccountSetUpdateComponent } from './account-set-update.component';

describe('AccountSet Management Update Component', () => {
  let comp: AccountSetUpdateComponent;
  let fixture: ComponentFixture<AccountSetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountSetFormService: AccountSetFormService;
  let accountSetService: AccountSetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccountSetUpdateComponent],
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
      .overrideTemplate(AccountSetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountSetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountSetFormService = TestBed.inject(AccountSetFormService);
    accountSetService = TestBed.inject(AccountSetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const accountSet: IAccountSet = { id: 25063 };

      activatedRoute.data = of({ accountSet });
      comp.ngOnInit();

      expect(comp.accountSet).toEqual(accountSet);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountSet>>();
      const accountSet = { id: 9279 };
      jest.spyOn(accountSetFormService, 'getAccountSet').mockReturnValue(accountSet);
      jest.spyOn(accountSetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountSet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountSet }));
      saveSubject.complete();

      // THEN
      expect(accountSetFormService.getAccountSet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountSetService.update).toHaveBeenCalledWith(expect.objectContaining(accountSet));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountSet>>();
      const accountSet = { id: 9279 };
      jest.spyOn(accountSetFormService, 'getAccountSet').mockReturnValue({ id: null });
      jest.spyOn(accountSetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountSet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountSet }));
      saveSubject.complete();

      // THEN
      expect(accountSetFormService.getAccountSet).toHaveBeenCalled();
      expect(accountSetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountSet>>();
      const accountSet = { id: 9279 };
      jest.spyOn(accountSetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountSet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountSetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
