jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMese, Mese } from '../mese.model';
import { MeseService } from '../service/mese.service';

import { MeseRoutingResolveService } from './mese-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mese routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MeseRoutingResolveService;
    let service: MeseService;
    let resultMese: IMese | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MeseRoutingResolveService);
      service = TestBed.inject(MeseService);
      resultMese = undefined;
    });

    describe('resolve', () => {
      it('should return IMese returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMese = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMese).toEqual({ id: 123 });
      });

      it('should return new IMese if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMese = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMese).toEqual(new Mese());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mese })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMese = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMese).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
