jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReservationService } from '../service/reservation.service';
import { IReservation, Reservation } from '../reservation.model';
import { IMese } from 'app/entities/mese/mese.model';
import { MeseService } from 'app/entities/mese/service/mese.service';

import { ReservationUpdateComponent } from './reservation-update.component';

describe('Component Tests', () => {
  describe('Reservation Management Update Component', () => {
    let comp: ReservationUpdateComponent;
    let fixture: ComponentFixture<ReservationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reservationService: ReservationService;
    let meseService: MeseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReservationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReservationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReservationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reservationService = TestBed.inject(ReservationService);
      meseService = TestBed.inject(MeseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Mese query and add missing value', () => {
        const reservation: IReservation = { id: 456 };
        const mese: IMese[] = [{ id: 32725 }];
        reservation.mese = mese;

        const meseCollection: IMese[] = [{ id: 66204 }];
        jest.spyOn(meseService, 'query').mockReturnValue(of(new HttpResponse({ body: meseCollection })));
        const additionalMese = [...mese];
        const expectedCollection: IMese[] = [...additionalMese, ...meseCollection];
        jest.spyOn(meseService, 'addMeseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ reservation });
        comp.ngOnInit();

        expect(meseService.query).toHaveBeenCalled();
        expect(meseService.addMeseToCollectionIfMissing).toHaveBeenCalledWith(meseCollection, ...additionalMese);
        expect(comp.meseSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const reservation: IReservation = { id: 456 };
        const mese: IMese = { id: 322 };
        reservation.mese = [mese];

        activatedRoute.data = of({ reservation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reservation));
        expect(comp.meseSharedCollection).toContain(mese);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reservation>>();
        const reservation = { id: 123 };
        jest.spyOn(reservationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reservation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reservation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reservationService.update).toHaveBeenCalledWith(reservation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reservation>>();
        const reservation = new Reservation();
        jest.spyOn(reservationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reservation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reservation }));
        saveSubject.complete();

        // THEN
        expect(reservationService.create).toHaveBeenCalledWith(reservation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Reservation>>();
        const reservation = { id: 123 };
        jest.spyOn(reservationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reservation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reservationService.update).toHaveBeenCalledWith(reservation);
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

    describe('Getting selected relationships', () => {
      describe('getSelectedMese', () => {
        it('Should return option if no Mese is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedMese(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Mese for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedMese(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Mese is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedMese(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
