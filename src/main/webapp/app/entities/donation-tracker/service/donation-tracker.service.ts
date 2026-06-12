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
import { IDonationTracker, NewDonationTracker } from '../donation-tracker.model';

export type PartialUpdateDonationTracker = Partial<IDonationTracker> & Pick<IDonationTracker, 'id'>;

type RestOf<T extends IDonationTracker | NewDonationTracker> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestDonationTracker = RestOf<IDonationTracker>;

export type NewRestDonationTracker = RestOf<NewDonationTracker>;

export type PartialUpdateRestDonationTracker = RestOf<PartialUpdateDonationTracker>;

export type EntityResponseType = HttpResponse<IDonationTracker>;
export type EntityArrayResponseType = HttpResponse<IDonationTracker[]>;

@Injectable({ providedIn: 'root' })
export class DonationTrackerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/donation-trackers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/donation-trackers/_search');

  create(donationTracker: NewDonationTracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTracker);
    return this.http
      .post<RestDonationTracker>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(donationTracker: IDonationTracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTracker);
    return this.http
      .put<RestDonationTracker>(`${this.resourceUrl}/${this.getDonationTrackerIdentifier(donationTracker)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(donationTracker: PartialUpdateDonationTracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTracker);
    return this.http
      .patch<RestDonationTracker>(`${this.resourceUrl}/${this.getDonationTrackerIdentifier(donationTracker)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDonationTracker>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDonationTracker[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDonationTracker[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDonationTracker[]>()], asapScheduler)),
    );
  }

  getDonationTrackerIdentifier(donationTracker: Pick<IDonationTracker, 'id'>): number {
    return donationTracker.id;
  }

  compareDonationTracker(o1: Pick<IDonationTracker, 'id'> | null, o2: Pick<IDonationTracker, 'id'> | null): boolean {
    return o1 && o2 ? this.getDonationTrackerIdentifier(o1) === this.getDonationTrackerIdentifier(o2) : o1 === o2;
  }

  addDonationTrackerToCollectionIfMissing<Type extends Pick<IDonationTracker, 'id'>>(
    donationTrackerCollection: Type[],
    ...donationTrackersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const donationTrackers: Type[] = donationTrackersToCheck.filter(isPresent);
    if (donationTrackers.length > 0) {
      const donationTrackerCollectionIdentifiers = donationTrackerCollection.map(donationTrackerItem =>
        this.getDonationTrackerIdentifier(donationTrackerItem),
      );
      const donationTrackersToAdd = donationTrackers.filter(donationTrackerItem => {
        const donationTrackerIdentifier = this.getDonationTrackerIdentifier(donationTrackerItem);
        if (donationTrackerCollectionIdentifiers.includes(donationTrackerIdentifier)) {
          return false;
        }
        donationTrackerCollectionIdentifiers.push(donationTrackerIdentifier);
        return true;
      });
      return [...donationTrackersToAdd, ...donationTrackerCollection];
    }
    return donationTrackerCollection;
  }

  protected convertDateFromClient<T extends IDonationTracker | NewDonationTracker | PartialUpdateDonationTracker>(
    donationTracker: T,
  ): RestOf<T> {
    return {
      ...donationTracker,
      date: donationTracker.date?.format(DATE_FORMAT) ?? null,
      createdDate: donationTracker.createdDate?.toJSON() ?? null,
      lastModifiedDate: donationTracker.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDonationTracker: RestDonationTracker): IDonationTracker {
    return {
      ...restDonationTracker,
      date: restDonationTracker.date ? dayjs(restDonationTracker.date) : undefined,
      createdDate: restDonationTracker.createdDate ? dayjs(restDonationTracker.createdDate) : undefined,
      lastModifiedDate: restDonationTracker.lastModifiedDate ? dayjs(restDonationTracker.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDonationTracker>): HttpResponse<IDonationTracker> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDonationTracker[]>): HttpResponse<IDonationTracker[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
