import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IChurchStaff, NewChurchStaff } from '../church-staff.model';

export type PartialUpdateChurchStaff = Partial<IChurchStaff> & Pick<IChurchStaff, 'id'>;

type RestOf<T extends IChurchStaff | NewChurchStaff> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestChurchStaff = RestOf<IChurchStaff>;

export type NewRestChurchStaff = RestOf<NewChurchStaff>;

export type PartialUpdateRestChurchStaff = RestOf<PartialUpdateChurchStaff>;

export type EntityResponseType = HttpResponse<IChurchStaff>;
export type EntityArrayResponseType = HttpResponse<IChurchStaff[]>;

@Injectable({ providedIn: 'root' })
export class ChurchStaffService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/church-staffs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/church-staffs/_search');

  create(churchStaff: NewChurchStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(churchStaff);
    return this.http
      .post<RestChurchStaff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(churchStaff: IChurchStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(churchStaff);
    return this.http
      .put<RestChurchStaff>(`${this.resourceUrl}/${this.getChurchStaffIdentifier(churchStaff)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(churchStaff: PartialUpdateChurchStaff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(churchStaff);
    return this.http
      .patch<RestChurchStaff>(`${this.resourceUrl}/${this.getChurchStaffIdentifier(churchStaff)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestChurchStaff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestChurchStaff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestChurchStaff[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IChurchStaff[]>()], asapScheduler)),
    );
  }

  getChurchStaffIdentifier(churchStaff: Pick<IChurchStaff, 'id'>): number {
    return churchStaff.id;
  }

  compareChurchStaff(o1: Pick<IChurchStaff, 'id'> | null, o2: Pick<IChurchStaff, 'id'> | null): boolean {
    return o1 && o2 ? this.getChurchStaffIdentifier(o1) === this.getChurchStaffIdentifier(o2) : o1 === o2;
  }

  addChurchStaffToCollectionIfMissing<Type extends Pick<IChurchStaff, 'id'>>(
    churchStaffCollection: Type[],
    ...churchStaffsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const churchStaffs: Type[] = churchStaffsToCheck.filter(isPresent);
    if (churchStaffs.length > 0) {
      const churchStaffCollectionIdentifiers = churchStaffCollection.map(churchStaffItem => this.getChurchStaffIdentifier(churchStaffItem));
      const churchStaffsToAdd = churchStaffs.filter(churchStaffItem => {
        const churchStaffIdentifier = this.getChurchStaffIdentifier(churchStaffItem);
        if (churchStaffCollectionIdentifiers.includes(churchStaffIdentifier)) {
          return false;
        }
        churchStaffCollectionIdentifiers.push(churchStaffIdentifier);
        return true;
      });
      return [...churchStaffsToAdd, ...churchStaffCollection];
    }
    return churchStaffCollection;
  }

  protected convertDateFromClient<T extends IChurchStaff | NewChurchStaff | PartialUpdateChurchStaff>(churchStaff: T): RestOf<T> {
    return {
      ...churchStaff,
      createdDate: churchStaff.createdDate?.toJSON() ?? null,
      lastModifiedDate: churchStaff.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restChurchStaff: RestChurchStaff): IChurchStaff {
    return {
      ...restChurchStaff,
      createdDate: restChurchStaff.createdDate ? dayjs(restChurchStaff.createdDate) : undefined,
      lastModifiedDate: restChurchStaff.lastModifiedDate ? dayjs(restChurchStaff.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestChurchStaff>): HttpResponse<IChurchStaff> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestChurchStaff[]>): HttpResponse<IChurchStaff[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
