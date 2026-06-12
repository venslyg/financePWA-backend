import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';

const assetCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(AssetCategoryService)
      .find(id)
      .pipe(
        mergeMap((assetCategory: HttpResponse<IAssetCategory>) => {
          if (assetCategory.body) {
            return of(assetCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default assetCategoryResolve;
