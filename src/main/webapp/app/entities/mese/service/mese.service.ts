import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMese, getMeseIdentifier } from '../mese.model';

export type EntityResponseType = HttpResponse<IMese>;
export type EntityArrayResponseType = HttpResponse<IMese[]>;

@Injectable({ providedIn: 'root' })
export class MeseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mese');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mese: IMese): Observable<EntityResponseType> {
    return this.http.post<IMese>(this.resourceUrl, mese, { observe: 'response' });
  }

  update(mese: IMese): Observable<EntityResponseType> {
    return this.http.put<IMese>(`${this.resourceUrl}/${getMeseIdentifier(mese) as number}`, mese, { observe: 'response' });
  }

  partialUpdate(mese: IMese): Observable<EntityResponseType> {
    return this.http.patch<IMese>(`${this.resourceUrl}/${getMeseIdentifier(mese) as number}`, mese, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMese>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMese[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeseToCollectionIfMissing(meseCollection: IMese[], ...meseToCheck: (IMese | null | undefined)[]): IMese[] {
    const mese: IMese[] = meseToCheck.filter(isPresent);
    if (mese.length > 0) {
      const meseCollectionIdentifiers = meseCollection.map(meseItem => getMeseIdentifier(meseItem)!);
      const meseToAdd = mese.filter(meseItem => {
        const meseIdentifier = getMeseIdentifier(meseItem);
        if (meseIdentifier == null || meseCollectionIdentifiers.includes(meseIdentifier)) {
          return false;
        }
        meseCollectionIdentifiers.push(meseIdentifier);
        return true;
      });
      return [...meseToAdd, ...meseCollection];
    }
    return meseCollection;
  }
}
