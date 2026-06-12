import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetRegister } from '../asset-register.model';
import { AssetRegisterService } from '../service/asset-register.service';
import { AssetRegisterFormGroup, AssetRegisterFormService } from './asset-register-form.service';

@Component({
  selector: 'jhi-asset-register-update',
  templateUrl: './asset-register-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetRegisterUpdateComponent implements OnInit {
  isSaving = false;
  assetRegister: IAssetRegister | null = null;

  protected assetRegisterService = inject(AssetRegisterService);
  protected assetRegisterFormService = inject(AssetRegisterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetRegisterFormGroup = this.assetRegisterFormService.createAssetRegisterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetRegister }) => {
      this.assetRegister = assetRegister;
      if (assetRegister) {
        this.updateForm(assetRegister);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetRegister = this.assetRegisterFormService.getAssetRegister(this.editForm);
    if (assetRegister.id !== null) {
      this.subscribeToSaveResponse(this.assetRegisterService.update(assetRegister));
    } else {
      this.subscribeToSaveResponse(this.assetRegisterService.create(assetRegister));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetRegister>>): void {
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

  protected updateForm(assetRegister: IAssetRegister): void {
    this.assetRegister = assetRegister;
    this.assetRegisterFormService.resetForm(this.editForm, assetRegister);
  }
}
