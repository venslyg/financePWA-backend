import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';

@Component({
  selector: 'jhi-asset-depreciation-history-detail',
  templateUrl: './asset-depreciation-history-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssetDepreciationHistoryDetailComponent {
  assetDepreciationHistory = input<IAssetDepreciationHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
