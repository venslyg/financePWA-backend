jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AssetDepreciationHistoryService } from '../service/asset-depreciation-history.service';

import { AssetDepreciationHistoryDeleteDialogComponent } from './asset-depreciation-history-delete-dialog.component';

describe('AssetDepreciationHistory Management Delete Component', () => {
  let comp: AssetDepreciationHistoryDeleteDialogComponent;
  let fixture: ComponentFixture<AssetDepreciationHistoryDeleteDialogComponent>;
  let service: AssetDepreciationHistoryService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssetDepreciationHistoryDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AssetDepreciationHistoryDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetDepreciationHistoryDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssetDepreciationHistoryService);
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
