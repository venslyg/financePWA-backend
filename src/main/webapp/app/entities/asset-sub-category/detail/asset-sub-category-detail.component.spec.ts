import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AssetSubCategoryDetailComponent } from './asset-sub-category-detail.component';

describe('AssetSubCategory Management Detail Component', () => {
  let comp: AssetSubCategoryDetailComponent;
  let fixture: ComponentFixture<AssetSubCategoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssetSubCategoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./asset-sub-category-detail.component').then(m => m.AssetSubCategoryDetailComponent),
              resolve: { assetSubCategory: () => of({ id: 24169 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AssetSubCategoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssetSubCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load assetSubCategory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AssetSubCategoryDetailComponent);

      // THEN
      expect(instance.assetSubCategory()).toEqual(expect.objectContaining({ id: 24169 }));
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
