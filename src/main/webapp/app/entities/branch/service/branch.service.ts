import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBranch, NewBranch } from '../branch.model';

export type PartialUpdateBranch = Partial<IBranch> & Pick<IBranch, 'id'>;

type RestOf<T extends IBranch | NewBranch> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBranch = RestOf<IBranch>;

export type NewRestBranch = RestOf<NewBranch>;

export type PartialUpdateRestBranch = RestOf<PartialUpdateBranch>;

export type EntityResponseType = HttpResponse<IBranch>;
export type EntityArrayResponseType = HttpResponse<IBranch[]>;

@Injectable({ providedIn: 'root' })
export class BranchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/branches');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/branches/_search');

  create(branch: NewBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .post<RestBranch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(branch: IBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .put<RestBranch>(`${this.resourceUrl}/${this.getBranchIdentifier(branch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(branch: PartialUpdateBranch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(branch);
    return this.http
      .patch<RestBranch>(`${this.resourceUrl}/${this.getBranchIdentifier(branch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBranch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBranch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBranch[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IBranch[]>()], asapScheduler)),
    );
  }

  getBranchIdentifier(branch: Pick<IBranch, 'id'>): number {
    return branch.id;
  }

  compareBranch(o1: Pick<IBranch, 'id'> | null, o2: Pick<IBranch, 'id'> | null): boolean {
    return o1 && o2 ? this.getBranchIdentifier(o1) === this.getBranchIdentifier(o2) : o1 === o2;
  }

  addBranchToCollectionIfMissing<Type extends Pick<IBranch, 'id'>>(
    branchCollection: Type[],
    ...branchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const branches: Type[] = branchesToCheck.filter(isPresent);
    if (branches.length > 0) {
      const branchCollectionIdentifiers = branchCollection.map(branchItem => this.getBranchIdentifier(branchItem));
      const branchesToAdd = branches.filter(branchItem => {
        const branchIdentifier = this.getBranchIdentifier(branchItem);
        if (branchCollectionIdentifiers.includes(branchIdentifier)) {
          return false;
        }
        branchCollectionIdentifiers.push(branchIdentifier);
        return true;
      });
      return [...branchesToAdd, ...branchCollection];
    }
    return branchCollection;
  }

  protected convertDateFromClient<T extends IBranch | NewBranch | PartialUpdateBranch>(branch: T): RestOf<T> {
    return {
      ...branch,
      createdDate: branch.createdDate?.toJSON() ?? null,
      lastModifiedDate: branch.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBranch: RestBranch): IBranch {
    return {
      ...restBranch,
      createdDate: restBranch.createdDate ? dayjs(restBranch.createdDate) : undefined,
      lastModifiedDate: restBranch.lastModifiedDate ? dayjs(restBranch.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBranch>): HttpResponse<IBranch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBranch[]>): HttpResponse<IBranch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
