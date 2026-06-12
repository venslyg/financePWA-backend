import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LiabilityLogDetailComponent } from './liability-log-detail.component';

describe('LiabilityLog Management Detail Component', () => {
  let comp: LiabilityLogDetailComponent;
  let fixture: ComponentFixture<LiabilityLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LiabilityLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./liability-log-detail.component').then(m => m.LiabilityLogDetailComponent),
              resolve: { liabilityLog: () => of({ id: 18609 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LiabilityLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LiabilityLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load liabilityLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LiabilityLogDetailComponent);

      // THEN
      expect(instance.liabilityLog()).toEqual(expect.objectContaining({ id: 18609 }));
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
