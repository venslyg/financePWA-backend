import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { IncomeEntryDetailComponent } from './income-entry-detail.component';

describe('IncomeEntry Management Detail Component', () => {
  let comp: IncomeEntryDetailComponent;
  let fixture: ComponentFixture<IncomeEntryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncomeEntryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./income-entry-detail.component').then(m => m.IncomeEntryDetailComponent),
              resolve: { incomeEntry: () => of({ id: 26683 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(IncomeEntryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IncomeEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load incomeEntry on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IncomeEntryDetailComponent);

      // THEN
      expect(instance.incomeEntry()).toEqual(expect.objectContaining({ id: 26683 }));
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
