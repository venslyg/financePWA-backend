import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AccountType } from 'app/entities/enumerations/account-type.model';
import { IAccountSet } from '../account-set.model';
import { AccountSetService } from '../service/account-set.service';
import { AccountSetFormGroup, AccountSetFormService } from './account-set-form.service';

@Component({
  selector: 'jhi-account-set-update',
  templateUrl: './account-set-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountSetUpdateComponent implements OnInit {
  isSaving = false;
  accountSet: IAccountSet | null = null;
  accountTypeValues = Object.keys(AccountType);

  protected accountSetService = inject(AccountSetService);
  protected accountSetFormService = inject(AccountSetFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountSetFormGroup = this.accountSetFormService.createAccountSetFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountSet }) => {
      this.accountSet = accountSet;
      if (accountSet) {
        this.updateForm(accountSet);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountSet = this.accountSetFormService.getAccountSet(this.editForm);
    if (accountSet.id !== null) {
      this.subscribeToSaveResponse(this.accountSetService.update(accountSet));
    } else {
      this.subscribeToSaveResponse(this.accountSetService.create(accountSet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountSet>>): void {
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

  protected updateForm(accountSet: IAccountSet): void {
    this.accountSet = accountSet;
    this.accountSetFormService.resetForm(this.editForm, accountSet);
  }
}
