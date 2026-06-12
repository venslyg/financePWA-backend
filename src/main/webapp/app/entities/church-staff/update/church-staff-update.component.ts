import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StaffType } from 'app/entities/enumerations/staff-type.model';
import { IChurchStaff } from '../church-staff.model';
import { ChurchStaffService } from '../service/church-staff.service';
import { ChurchStaffFormGroup, ChurchStaffFormService } from './church-staff-form.service';

@Component({
  selector: 'jhi-church-staff-update',
  templateUrl: './church-staff-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChurchStaffUpdateComponent implements OnInit {
  isSaving = false;
  churchStaff: IChurchStaff | null = null;
  staffTypeValues = Object.keys(StaffType);

  protected churchStaffService = inject(ChurchStaffService);
  protected churchStaffFormService = inject(ChurchStaffFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChurchStaffFormGroup = this.churchStaffFormService.createChurchStaffFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ churchStaff }) => {
      this.churchStaff = churchStaff;
      if (churchStaff) {
        this.updateForm(churchStaff);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const churchStaff = this.churchStaffFormService.getChurchStaff(this.editForm);
    if (churchStaff.id !== null) {
      this.subscribeToSaveResponse(this.churchStaffService.update(churchStaff));
    } else {
      this.subscribeToSaveResponse(this.churchStaffService.create(churchStaff));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurchStaff>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(churchStaff: IChurchStaff): void {
    this.churchStaff = churchStaff;
    this.churchStaffFormService.resetForm(this.editForm, churchStaff);
  }
}
