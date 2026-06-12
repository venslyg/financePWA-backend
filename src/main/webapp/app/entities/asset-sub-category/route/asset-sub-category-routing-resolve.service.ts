import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetSubCategory } from '../asset-sub-category.model';
import { AssetSubCategoryService } from '../service/asset-sub-category.service';

const assetSubCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetSubCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(AssetSubCategoryService)
      .find(id)
      .pipe(
        mergeMap((assetSubCategory: HttpResponse<IAssetSubCategory>) => {
          if (assetSubCategory.body) {
            return of(assetSubCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default assetSubCategoryResolve;
