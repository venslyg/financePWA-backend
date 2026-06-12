import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExpenseSubCategoryDetailComponent } from './expense-sub-category-detail.component';

describe('ExpenseSubCategory Management Detail Component', () => {
  let comp: ExpenseSubCategoryDetailComponent;
  let fixture: ComponentFixture<ExpenseSubCategoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpenseSubCategoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./expense-sub-category-detail.component').then(m => m.ExpenseSubCategoryDetailComponent),
              resolve: { expenseSubCategory: () => of({ id: 23454 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExpenseSubCategoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpenseSubCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load expenseSubCategory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExpenseSubCategoryDetailComponent);

      // THEN
      expect(instance.expenseSubCategory()).toEqual(expect.objectContaining({ id: 23454 }));
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
