import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AssetRegisterService } from '../service/asset-register.service';
import { IAssetRegister } from '../asset-register.model';
import { AssetRegisterFormService } from './asset-register-form.service';

import { AssetRegisterUpdateComponent } from './asset-register-update.component';

describe('AssetRegister Management Update Component', () => {
  let comp: AssetRegisterUpdateComponent;
  let fixture: ComponentFixture<AssetRegisterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetRegisterFormService: AssetRegisterFormService;
  let assetRegisterService: AssetRegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssetRegisterUpdateComponent],
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
      .overrideTemplate(AssetRegisterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetRegisterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetRegisterFormService = TestBed.inject(AssetRegisterFormService);
    assetRegisterService = TestBed.inject(AssetRegisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const assetRegister: IAssetRegister = { id: 5579 };

      activatedRoute.data = of({ assetRegister });
      comp.ngOnInit();

      expect(comp.assetRegister).toEqual(assetRegister);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetRegister>>();
      const assetRegister = { id: 25580 };
      jest.spyOn(assetRegisterFormService, 'getAssetRegister').mockReturnValue(assetRegister);
      jest.spyOn(assetRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetRegister }));
      saveSubject.complete();

      // THEN
      expect(assetRegisterFormService.getAssetRegister).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetRegisterService.update).toHaveBeenCalledWith(expect.objectContaining(assetRegister));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetRegister>>();
      const assetRegister = { id: 25580 };
      jest.spyOn(assetRegisterFormService, 'getAssetRegister').mockReturnValue({ id: null });
      jest.spyOn(assetRegisterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRegister: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetRegister }));
      saveSubject.complete();

      // THEN
      expect(assetRegisterFormService.getAssetRegister).toHaveBeenCalled();
      expect(assetRegisterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetRegister>>();
      const assetRegister = { id: 25580 };
      jest.spyOn(assetRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetRegisterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
