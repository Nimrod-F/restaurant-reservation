import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MeseDetailComponent } from './mese-detail.component';

describe('Component Tests', () => {
  describe('Mese Management Detail Component', () => {
    let comp: MeseDetailComponent;
    let fixture: ComponentFixture<MeseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MeseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mese: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MeseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MeseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mese on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mese).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
