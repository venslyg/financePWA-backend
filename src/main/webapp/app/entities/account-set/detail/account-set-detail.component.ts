import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAccountSet } from '../account-set.model';

@Component({
  selector: 'jhi-account-set-detail',
  templateUrl: './account-set-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AccountSetDetailComponent {
  accountSet = input<IAccountSet | null>(null);

  previousState(): void {
    window.history.back();
  }
}
