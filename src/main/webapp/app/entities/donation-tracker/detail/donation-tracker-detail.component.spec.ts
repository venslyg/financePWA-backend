import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DonationTrackerDetailComponent } from './donation-tracker-detail.component';

describe('DonationTracker Management Detail Component', () => {
  let comp: DonationTrackerDetailComponent;
  let fixture: ComponentFixture<DonationTrackerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DonationTrackerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./donation-tracker-detail.component').then(m => m.DonationTrackerDetailComponent),
              resolve: { donationTracker: () => of({ id: 16869 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DonationTrackerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DonationTrackerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load donationTracker on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DonationTrackerDetailComponent);

      // THEN
      expect(instance.donationTracker()).toEqual(expect.objectContaining({ id: 16869 }));
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
