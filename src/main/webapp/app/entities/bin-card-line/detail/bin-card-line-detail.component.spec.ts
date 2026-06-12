import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BinCardLineDetailComponent } from './bin-card-line-detail.component';

describe('BinCardLine Management Detail Component', () => {
  let comp: BinCardLineDetailComponent;
  let fixture: ComponentFixture<BinCardLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BinCardLineDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bin-card-line-detail.component').then(m => m.BinCardLineDetailComponent),
              resolve: { binCardLine: () => of({ id: 10587 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BinCardLineDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BinCardLineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load binCardLine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BinCardLineDetailComponent);

      // THEN
      expect(instance.binCardLine()).toEqual(expect.objectContaining({ id: 10587 }));
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
