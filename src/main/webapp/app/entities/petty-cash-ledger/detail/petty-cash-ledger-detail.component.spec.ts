import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PettyCashLedgerDetailComponent } from './petty-cash-ledger-detail.component';

describe('PettyCashLedger Management Detail Component', () => {
  let comp: PettyCashLedgerDetailComponent;
  let fixture: ComponentFixture<PettyCashLedgerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PettyCashLedgerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./petty-cash-ledger-detail.component').then(m => m.PettyCashLedgerDetailComponent),
              resolve: { pettyCashLedger: () => of({ id: 23503 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PettyCashLedgerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PettyCashLedgerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pettyCashLedger on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PettyCashLedgerDetailComponent);

      // THEN
      expect(instance.pettyCashLedger()).toEqual(expect.objectContaining({ id: 23503 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
