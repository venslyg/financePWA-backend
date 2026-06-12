import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetDepreciationHistory } from '../asset-depreciation-history.model';
import { AssetDepreciationHistoryService } from '../service/asset-depreciation-history.service';

const assetDepreciationHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetDepreciationHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(AssetDepreciationHistoryService)
      .find(id)
      .pipe(
        mergeMap((assetDepreciationHistory: HttpResponse<IAssetDepreciationHistory>) => {
          if (assetDepreciationHistory.body) {
            return of(assetDepreciationHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default assetDepreciationHistoryResolve;
