import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAssetSubCategory } from '../asset-sub-category.model';

@Component({
  selector: 'jhi-asset-sub-category-detail',
  templateUrl: './asset-sub-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AssetSubCategoryDetailComponent {
  assetSubCategory = input<IAssetSubCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
