import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAssetRegister, NewAssetRegister } from '../asset-register.model';

export type PartialUpdateAssetRegister = Partial<IAssetRegister> & Pick<IAssetRegister, 'id'>;

type RestOf<T extends IAssetRegister | NewAssetRegister> = Omit<T, 'purchaseDate' | 'createdDate' | 'lastModifiedDate'> & {
  purchaseDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestAssetRegister = RestOf<IAssetRegister>;

export type NewRestAssetRegister = RestOf<NewAssetRegister>;

export type PartialUpdateRestAssetRegister = RestOf<PartialUpdateAssetRegister>;

export type EntityResponseType = HttpResponse<IAssetRegister>;
export type EntityArrayResponseType = HttpResponse<IAssetRegister[]>;

@Injectable({ providedIn: 'root' })
export class AssetRegisterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-registers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/asset-registers/_search');

  create(assetRegister: NewAssetRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegister);
    return this.http
      .post<RestAssetRegister>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(assetRegister: IAssetRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegister);
    return this.http
      .put<RestAssetRegister>(`${this.resourceUrl}/${this.getAssetRegisterIdentifier(assetRegister)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(assetRegister: PartialUpdateAssetRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegister);
    return this.http
      .patch<RestAssetRegister>(`${this.resourceUrl}/${this.getAssetRegisterIdentifier(assetRegister)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAssetRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAssetRegister[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAssetRegister[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAssetRegister[]>()], asapScheduler)),
    );
  }

  getAssetRegisterIdentifier(assetRegister: Pick<IAssetRegister, 'id'>): number {
    return assetRegister.id;
  }

  compareAssetRegister(o1: Pick<IAssetRegister, 'id'> | null, o2: Pick<IAssetRegister, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetRegisterIdentifier(o1) === this.getAssetRegisterIdentifier(o2) : o1 === o2;
  }

  addAssetRegisterToCollectionIfMissing<Type extends Pick<IAssetRegister, 'id'>>(
    assetRegisterCollection: Type[],
    ...assetRegistersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetRegisters: Type[] = assetRegistersToCheck.filter(isPresent);
    if (assetRegisters.length > 0) {
      const assetRegisterCollectionIdentifiers = assetRegisterCollection.map(assetRegisterItem =>
        this.getAssetRegisterIdentifier(assetRegisterItem),
      );
      const assetRegistersToAdd = assetRegisters.filter(assetRegisterItem => {
        const assetRegisterIdentifier = this.getAssetRegisterIdentifier(assetRegisterItem);
        if (assetRegisterCollectionIdentifiers.includes(assetRegisterIdentifier)) {
          return false;
        }
        assetRegisterCollectionIdentifiers.push(assetRegisterIdentifier);
        return true;
      });
      return [...assetRegistersToAdd, ...assetRegisterCollection];
    }
    return assetRegisterCollection;
  }

  protected convertDateFromClient<T extends IAssetRegister | NewAssetRegister | PartialUpdateAssetRegister>(assetRegister: T): RestOf<T> {
    return {
      ...assetRegister,
      purchaseDate: assetRegister.purchaseDate?.format(DATE_FORMAT) ?? null,
      createdDate: assetRegister.createdDate?.toJSON() ?? null,
      lastModifiedDate: assetRegister.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAssetRegister: RestAssetRegister): IAssetRegister {
    return {
      ...restAssetRegister,
      purchaseDate: restAssetRegister.purchaseDate ? dayjs(restAssetRegister.purchaseDate) : undefined,
      createdDate: restAssetRegister.createdDate ? dayjs(restAssetRegister.createdDate) : undefined,
      lastModifiedDate: restAssetRegister.lastModifiedDate ? dayjs(restAssetRegister.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAssetRegister>): HttpResponse<IAssetRegister> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAssetRegister[]>): HttpResponse<IAssetRegister[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
