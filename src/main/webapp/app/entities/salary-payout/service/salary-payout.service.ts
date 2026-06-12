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
import { ISalaryPayout, NewSalaryPayout } from '../salary-payout.model';

export type PartialUpdateSalaryPayout = Partial<ISalaryPayout> & Pick<ISalaryPayout, 'id'>;

type RestOf<T extends ISalaryPayout | NewSalaryPayout> = Omit<T, 'payoutDate' | 'createdDate' | 'lastModifiedDate'> & {
  payoutDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestSalaryPayout = RestOf<ISalaryPayout>;

export type NewRestSalaryPayout = RestOf<NewSalaryPayout>;

export type PartialUpdateRestSalaryPayout = RestOf<PartialUpdateSalaryPayout>;

export type EntityResponseType = HttpResponse<ISalaryPayout>;
export type EntityArrayResponseType = HttpResponse<ISalaryPayout[]>;

@Injectable({ providedIn: 'root' })
export class SalaryPayoutService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salary-payouts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/salary-payouts/_search');

  create(salaryPayout: NewSalaryPayout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryPayout);
    return this.http
      .post<RestSalaryPayout>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(salaryPayout: ISalaryPayout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryPayout);
    return this.http
      .put<RestSalaryPayout>(`${this.resourceUrl}/${this.getSalaryPayoutIdentifier(salaryPayout)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(salaryPayout: PartialUpdateSalaryPayout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryPayout);
    return this.http
      .patch<RestSalaryPayout>(`${this.resourceUrl}/${this.getSalaryPayoutIdentifier(salaryPayout)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSalaryPayout>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSalaryPayout[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSalaryPayout[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISalaryPayout[]>()], asapScheduler)),
    );
  }

  getSalaryPayoutIdentifier(salaryPayout: Pick<ISalaryPayout, 'id'>): number {
    return salaryPayout.id;
  }

  compareSalaryPayout(o1: Pick<ISalaryPayout, 'id'> | null, o2: Pick<ISalaryPayout, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryPayoutIdentifier(o1) === this.getSalaryPayoutIdentifier(o2) : o1 === o2;
  }

  addSalaryPayoutToCollectionIfMissing<Type extends Pick<ISalaryPayout, 'id'>>(
    salaryPayoutCollection: Type[],
    ...salaryPayoutsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaryPayouts: Type[] = salaryPayoutsToCheck.filter(isPresent);
    if (salaryPayouts.length > 0) {
      const salaryPayoutCollectionIdentifiers = salaryPayoutCollection.map(salaryPayoutItem =>
        this.getSalaryPayoutIdentifier(salaryPayoutItem),
      );
      const salaryPayoutsToAdd = salaryPayouts.filter(salaryPayoutItem => {
        const salaryPayoutIdentifier = this.getSalaryPayoutIdentifier(salaryPayoutItem);
        if (salaryPayoutCollectionIdentifiers.includes(salaryPayoutIdentifier)) {
          return false;
        }
        salaryPayoutCollectionIdentifiers.push(salaryPayoutIdentifier);
        return true;
      });
      return [...salaryPayoutsToAdd, ...salaryPayoutCollection];
    }
    return salaryPayoutCollection;
  }

  protected convertDateFromClient<T extends ISalaryPayout | NewSalaryPayout | PartialUpdateSalaryPayout>(salaryPayout: T): RestOf<T> {
    return {
      ...salaryPayout,
      payoutDate: salaryPayout.payoutDate?.format(DATE_FORMAT) ?? null,
      createdDate: salaryPayout.createdDate?.toJSON() ?? null,
      lastModifiedDate: salaryPayout.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSalaryPayout: RestSalaryPayout): ISalaryPayout {
    return {
      ...restSalaryPayout,
      payoutDate: restSalaryPayout.payoutDate ? dayjs(restSalaryPayout.payoutDate) : undefined,
      createdDate: restSalaryPayout.createdDate ? dayjs(restSalaryPayout.createdDate) : undefined,
      lastModifiedDate: restSalaryPayout.lastModifiedDate ? dayjs(restSalaryPayout.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSalaryPayout>): HttpResponse<ISalaryPayout> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSalaryPayout[]>): HttpResponse<ISalaryPayout[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
