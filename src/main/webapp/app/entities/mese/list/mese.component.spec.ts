import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MeseService } from '../service/mese.service';

import { MeseComponent } from './mese.component';

describe('Component Tests', () => {
  describe('Mese Management Component', () => {
    let comp: MeseComponent;
    let fixture: ComponentFixture<MeseComponent>;
    let service: MeseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MeseComponent],
      })
        .overrideTemplate(MeseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MeseComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MeseService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.mese?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
