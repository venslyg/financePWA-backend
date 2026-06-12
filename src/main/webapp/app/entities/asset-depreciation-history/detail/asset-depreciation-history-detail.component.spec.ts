import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AssetDepreciationHistoryDetailComponent } from './asset-depreciation-history-detail.component';

describe('AssetDepreciationHistory Management Detail Component', () => {
  let comp: AssetDepreciationHistoryDetailComponent;
  let fixture: ComponentFixture<AssetDepreciationHistoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssetDepreciationHistoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./asset-depreciation-history-detail.component').then(m => m.AssetDepreciationHistoryDetailComponent),
              resolve: { assetDepreciationHistory: () => of({ id: 23291 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AssetDepreciationHistoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssetDepreciationHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load assetDepreciationHistory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AssetDepreciationHistoryDetailComponent);

      // THEN
      expect(instance.assetDepreciationHistory()).toEqual(expect.objectContaining({ id: 23291 }));
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
