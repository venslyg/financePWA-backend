import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetRegister } from '../asset-register.model';
import { AssetRegisterService } from '../service/asset-register.service';

const assetRegisterResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetRegister> => {
  const id = route.params.id;
  if (id) {
    return inject(AssetRegisterService)
      .find(id)
      .pipe(
        mergeMap((assetRegister: HttpResponse<IAssetRegister>) => {
          if (assetRegister.body) {
            return of(assetRegister.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default assetRegisterResolve;
