import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAssetCategory } from '../asset-category.model';

@Component({
  selector: 'jhi-asset-category-detail',
  templateUrl: './asset-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AssetCategoryDetailComponent {
  assetCategory = input<IAssetCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
