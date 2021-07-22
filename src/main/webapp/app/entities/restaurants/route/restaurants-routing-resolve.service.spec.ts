jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRestaurants, Restaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';

import { RestaurantsRoutingResolveService } from './restaurants-routing-resolve.service';

describe('Service Tests', () => {
  describe('Restaurants routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RestaurantsRoutingResolveService;
    let service: RestaurantsService;
    let resultRestaurants: IRestaurants | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RestaurantsRoutingResolveService);
      service = TestBed.inject(RestaurantsService);
      resultRestaurants = undefined;
    });

    describe('resolve', () => {
      it('should return IRestaurants returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRestaurants = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRestaurants).toEqual({ id: 123 });
      });

      it('should return new IRestaurants if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRestaurants = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRestaurants).toEqual(new Restaurants());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Restaurants })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRestaurants = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRestaurants).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
