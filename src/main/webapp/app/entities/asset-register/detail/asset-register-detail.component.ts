import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAssetRegister } from '../asset-register.model';

@Component({
  selector: 'jhi-asset-register-detail',
  templateUrl: './asset-register-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssetRegisterDetailComponent {
  assetRegister = input<IAssetRegister | null>(null);

  previousState(): void {
    window.history.back();
  }
}
