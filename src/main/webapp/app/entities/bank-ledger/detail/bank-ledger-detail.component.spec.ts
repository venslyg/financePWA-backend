import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BankLedgerDetailComponent } from './bank-ledger-detail.component';

describe('BankLedger Management Detail Component', () => {
  let comp: BankLedgerDetailComponent;
  let fixture: ComponentFixture<BankLedgerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankLedgerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bank-ledger-detail.component').then(m => m.BankLedgerDetailComponent),
              resolve: { bankLedger: () => of({ id: 4046 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BankLedgerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BankLedgerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load bankLedger on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BankLedgerDetailComponent);

      // THEN
      expect(instance.bankLedger()).toEqual(expect.objectContaining({ id: 4046 }));
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
