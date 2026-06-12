jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExpenseSubCategoryService } from '../service/expense-sub-category.service';

import { ExpenseSubCategoryDeleteDialogComponent } from './expense-sub-category-delete-dialog.component';

describe('ExpenseSubCategory Management Delete Component', () => {
  let comp: ExpenseSubCategoryDeleteDialogComponent;
  let fixture: ComponentFixture<ExpenseSubCategoryDeleteDialogComponent>;
  let service: ExpenseSubCategoryService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExpenseSubCategoryDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ExpenseSubCategoryDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpenseSubCategoryDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ExpenseSubCategoryService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
