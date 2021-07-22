import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMese, Mese } from '../mese.model';
import { MeseService } from '../service/mese.service';

@Injectable({ providedIn: 'root' })
export class MeseRoutingResolveService implements Resolve<IMese> {
  constructor(protected service: MeseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMese> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mese: HttpResponse<Mese>) => {
          if (mese.body) {
            return of(mese.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mese());
  }
}
