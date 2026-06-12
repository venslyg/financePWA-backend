import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ChurchStaffDetailComponent } from './church-staff-detail.component';

describe('ChurchStaff Management Detail Component', () => {
  let comp: ChurchStaffDetailComponent;
  let fixture: ComponentFixture<ChurchStaffDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChurchStaffDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./church-staff-detail.component').then(m => m.ChurchStaffDetailComponent),
              resolve: { churchStaff: () => of({ id: 18086 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ChurchStaffDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChurchStaffDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load churchStaff on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ChurchStaffDetailComponent);

      // THEN
      expect(instance.churchStaff()).toEqual(expect.objectContaining({ id: 18086 }));
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
