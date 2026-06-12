import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DonationTrackerService } from '../service/donation-tracker.service';
import { IDonationTracker } from '../donation-tracker.model';
import { DonationTrackerFormService } from './donation-tracker-form.service';

import { DonationTrackerUpdateComponent } from './donation-tracker-update.component';

describe('DonationTracker Management Update Component', () => {
  let comp: DonationTrackerUpdateComponent;
  let fixture: ComponentFixture<DonationTrackerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donationTrackerFormService: DonationTrackerFormService;
  let donationTrackerService: DonationTrackerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DonationTrackerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DonationTrackerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonationTrackerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donationTrackerFormService = TestBed.inject(DonationTrackerFormService);
    donationTrackerService = TestBed.inject(DonationTrackerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const donationTracker: IDonationTracker = { id: 18263 };

      activatedRoute.data = of({ donationTracker });
      comp.ngOnInit();

      expect(comp.donationTracker).toEqual(donationTracker);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTracker>>();
      const donationTracker = { id: 16869 };
      jest.spyOn(donationTrackerFormService, 'getDonationTracker').mockReturnValue(donationTracker);
      jest.spyOn(donationTrackerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTracker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donationTracker }));
      saveSubject.complete();

      // THEN
      expect(donationTrackerFormService.getDonationTracker).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(donationTrackerService.update).toHaveBeenCalledWith(expect.objectContaining(donationTracker));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTracker>>();
      const donationTracker = { id: 16869 };
      jest.spyOn(donationTrackerFormService, 'getDonationTracker').mockReturnValue({ id: null });
      jest.spyOn(donationTrackerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTracker: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donationTracker }));
      saveSubject.complete();

      // THEN
      expect(donationTrackerFormService.getDonationTracker).toHaveBeenCalled();
      expect(donationTrackerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTracker>>();
      const donationTracker = { id: 16869 };
      jest.spyOn(donationTrackerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTracker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donationTrackerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
