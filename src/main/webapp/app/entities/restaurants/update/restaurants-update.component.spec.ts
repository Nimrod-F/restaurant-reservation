jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RestaurantsService } from '../service/restaurants.service';
import { IRestaurants, Restaurants } from '../restaurants.model';
import { IMese } from 'app/entities/mese/mese.model';
import { MeseService } from 'app/entities/mese/service/mese.service';

import { RestaurantsUpdateComponent } from './restaurants-update.component';

describe('Component Tests', () => {
  describe('Restaurants Management Update Component', () => {
    let comp: RestaurantsUpdateComponent;
    let fixture: ComponentFixture<RestaurantsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let restaurantsService: RestaurantsService;
    let meseService: MeseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RestaurantsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RestaurantsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RestaurantsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      restaurantsService = TestBed.inject(RestaurantsService);
      meseService = TestBed.inject(MeseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Mese query and add missing value', () => {
        const restaurants: IRestaurants = { id: 456 };
        const mese: IMese = { id: 6846 };
        restaurants.mese = mese;

        const meseCollection: IMese[] = [{ id: 11031 }];
        jest.spyOn(meseService, 'query').mockReturnValue(of(new HttpResponse({ body: meseCollection })));
        const additionalMese = [mese];
        const expectedCollection: IMese[] = [...additionalMese, ...meseCollection];
        jest.spyOn(meseService, 'addMeseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ restaurants });
        comp.ngOnInit();

        expect(meseService.query).toHaveBeenCalled();
        expect(meseService.addMeseToCollectionIfMissing).toHaveBeenCalledWith(meseCollection, ...additionalMese);
        expect(comp.meseSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const restaurants: IRestaurants = { id: 456 };
        const mese: IMese = { id: 82157 };
        restaurants.mese = mese;

        activatedRoute.data = of({ restaurants });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(restaurants));
        expect(comp.meseSharedCollection).toContain(mese);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Restaurants>>();
        const restaurants = { id: 123 };
        jest.spyOn(restaurantsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurants });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurants }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(restaurantsService.update).toHaveBeenCalledWith(restaurants);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Restaurants>>();
        const restaurants = new Restaurants();
        jest.spyOn(restaurantsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurants });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurants }));
        saveSubject.complete();

        // THEN
        expect(restaurantsService.create).toHaveBeenCalledWith(restaurants);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Restaurants>>();
        const restaurants = { id: 123 };
        jest.spyOn(restaurantsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurants });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(restaurantsService.update).toHaveBeenCalledWith(restaurants);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMeseById', () => {
        it('Should return tracked Mese primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMeseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
