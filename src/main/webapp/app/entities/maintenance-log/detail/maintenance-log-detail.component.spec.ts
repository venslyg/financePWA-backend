import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaintenanceLogDetailComponent } from './maintenance-log-detail.component';

describe('MaintenanceLog Management Detail Component', () => {
  let comp: MaintenanceLogDetailComponent;
  let fixture: ComponentFixture<MaintenanceLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaintenanceLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./maintenance-log-detail.component').then(m => m.MaintenanceLogDetailComponent),
              resolve: { maintenanceLog: () => of({ id: 18508 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaintenanceLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaintenanceLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load maintenanceLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaintenanceLogDetailComponent);

      // THEN
      expect(instance.maintenanceLog()).toEqual(expect.objectContaining({ id: 18508 }));
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
