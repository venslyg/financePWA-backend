import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SalaryPayoutDetailComponent } from './salary-payout-detail.component';

describe('SalaryPayout Management Detail Component', () => {
  let comp: SalaryPayoutDetailComponent;
  let fixture: ComponentFixture<SalaryPayoutDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalaryPayoutDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./salary-payout-detail.component').then(m => m.SalaryPayoutDetailComponent),
              resolve: { salaryPayout: () => of({ id: 24337 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SalaryPayoutDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryPayoutDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load salaryPayout on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SalaryPayoutDetailComponent);

      // THEN
      expect(instance.salaryPayout()).toEqual(expect.objectContaining({ id: 24337 }));
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
