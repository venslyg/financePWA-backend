import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetRegister } from 'app/entities/asset-register/asset-register.model';
import { AssetRegisterService } from 'app/entities/asset-register/service/asset-register.service';
import { MaintenanceLogType } from 'app/entities/enumerations/maintenance-log-type.model';
import { MaintenanceLogService } from '../service/maintenance-log.service';
import { IMaintenanceLog } from '../maintenance-log.model';
import { MaintenanceLogFormGroup, MaintenanceLogFormService } from './maintenance-log-form.service';

@Component({
  selector: 'jhi-maintenance-log-update',
  templateUrl: './maintenance-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaintenanceLogUpdateComponent implements OnInit {
  isSaving = false;
  maintenanceLog: IMaintenanceLog | null = null;
  maintenanceLogTypeValues = Object.keys(MaintenanceLogType);

  assetRegistersSharedCollection: IAssetRegister[] = [];

  protected maintenanceLogService = inject(MaintenanceLogService);
  protected maintenanceLogFormService = inject(MaintenanceLogFormService);
  protected assetRegisterService = inject(AssetRegisterService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaintenanceLogFormGroup = this.maintenanceLogFormService.createMaintenanceLogFormGroup();

  compareAssetRegister = (o1: IAssetRegister | null, o2: IAssetRegister | null): boolean =>
    this.assetRegisterService.compareAssetRegister(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintenanceLog }) => {
      this.maintenanceLog = maintenanceLog;
      if (maintenanceLog) {
        this.updateForm(maintenanceLog);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maintenanceLog = this.maintenanceLogFormService.getMaintenanceLog(this.editForm);
    if (maintenanceLog.id !== null) {
      this.subscribeToSaveResponse(this.maintenanceLogService.update(maintenanceLog));
    } else {
      this.subscribeToSaveResponse(this.maintenanceLogService.create(maintenanceLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintenanceLog>>): void {
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

  protected updateForm(maintenanceLog: IMaintenanceLog): void {
    this.maintenanceLog = maintenanceLog;
    this.maintenanceLogFormService.resetForm(this.editForm, maintenanceLog);

    this.assetRegistersSharedCollection = this.assetRegisterService.addAssetRegisterToCollectionIfMissing<IAssetRegister>(
      this.assetRegistersSharedCollection,
      maintenanceLog.asset,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetRegisterService
      .query()
      .pipe(map((res: HttpResponse<IAssetRegister[]>) => res.body ?? []))
      .pipe(
        map((assetRegisters: IAssetRegister[]) =>
          this.assetRegisterService.addAssetRegisterToCollectionIfMissing<IAssetRegister>(assetRegisters, this.maintenanceLog?.asset),
        ),
      )
      .subscribe((assetRegisters: IAssetRegister[]) => (this.assetRegistersSharedCollection = assetRegisters));
  }
}
