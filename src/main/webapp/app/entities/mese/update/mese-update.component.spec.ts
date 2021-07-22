jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MeseService } from '../service/mese.service';
import { IMese, Mese } from '../mese.model';

import { MeseUpdateComponent } from './mese-update.component';

describe('Component Tests', () => {
  describe('Mese Management Update Component', () => {
    let comp: MeseUpdateComponent;
    let fixture: ComponentFixture<MeseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let meseService: MeseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MeseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MeseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MeseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      meseService = TestBed.inject(MeseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const mese: IMese = { id: 456 };

        activatedRoute.data = of({ mese });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mese));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mese>>();
        const mese = { id: 123 };
        jest.spyOn(meseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mese });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mese }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(meseService.update).toHaveBeenCalledWith(mese);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mese>>();
        const mese = new Mese();
        jest.spyOn(meseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mese });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mese }));
        saveSubject.complete();

        // THEN
        expect(meseService.create).toHaveBeenCalledWith(mese);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mese>>();
        const mese = { id: 123 };
        jest.spyOn(meseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mese });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(meseService.update).toHaveBeenCalledWith(mese);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
