import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { InventoryItemService } from '../service/inventory-item.service';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemFormService } from './inventory-item-form.service';

import { InventoryItemUpdateComponent } from './inventory-item-update.component';

describe('InventoryItem Management Update Component', () => {
  let comp: InventoryItemUpdateComponent;
  let fixture: ComponentFixture<InventoryItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemFormService: InventoryItemFormService;
  let inventoryItemService: InventoryItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InventoryItemUpdateComponent],
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
      .overrideTemplate(InventoryItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventoryItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemFormService = TestBed.inject(InventoryItemFormService);
    inventoryItemService = TestBed.inject(InventoryItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const inventoryItem: IInventoryItem = { id: 4332 };

      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      expect(comp.inventoryItem).toEqual(inventoryItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue(inventoryItem);
      jest.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryItem }));
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemFormService, 'getInventoryItem').mockReturnValue({ id: null });
      jest.spyOn(inventoryItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryItem }));
      saveSubject.complete();

      // THEN
      expect(inventoryItemFormService.getInventoryItem).toHaveBeenCalled();
      expect(inventoryItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryItem>>();
      const inventoryItem = { id: 7462 };
      jest.spyOn(inventoryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
