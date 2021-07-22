import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMese, Mese } from '../mese.model';

import { MeseService } from './mese.service';

describe('Service Tests', () => {
  describe('Mese Service', () => {
    let service: MeseService;
    let httpMock: HttpTestingController;
    let elemDefault: IMese;
    let expectedResult: IMese | IMese[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MeseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nrLoc: 0,
        outdoor: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Mese', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Mese()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mese', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nrLoc: 1,
            outdoor: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mese', () => {
        const patchObject = Object.assign(
          {
            outdoor: true,
          },
          new Mese()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mese', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nrLoc: 1,
            outdoor: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Mese', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMeseToCollectionIfMissing', () => {
        it('should add a Mese to an empty array', () => {
          const mese: IMese = { id: 123 };
          expectedResult = service.addMeseToCollectionIfMissing([], mese);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mese);
        });

        it('should not add a Mese to an array that contains it', () => {
          const mese: IMese = { id: 123 };
          const meseCollection: IMese[] = [
            {
              ...mese,
            },
            { id: 456 },
          ];
          expectedResult = service.addMeseToCollectionIfMissing(meseCollection, mese);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mese to an array that doesn't contain it", () => {
          const mese: IMese = { id: 123 };
          const meseCollection: IMese[] = [{ id: 456 }];
          expectedResult = service.addMeseToCollectionIfMissing(meseCollection, mese);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mese);
        });

        it('should add only unique Mese to an array', () => {
          const meseArray: IMese[] = [{ id: 123 }, { id: 456 }, { id: 31324 }];
          const meseCollection: IMese[] = [{ id: 123 }];
          expectedResult = service.addMeseToCollectionIfMissing(meseCollection, ...meseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mese: IMese = { id: 123 };
          const mese2: IMese = { id: 456 };
          expectedResult = service.addMeseToCollectionIfMissing([], mese, mese2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mese);
          expect(expectedResult).toContain(mese2);
        });

        it('should accept null and undefined values', () => {
          const mese: IMese = { id: 123 };
          expectedResult = service.addMeseToCollectionIfMissing([], null, mese, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mese);
        });

        it('should return initial array if no Mese is added', () => {
          const meseCollection: IMese[] = [{ id: 123 }];
          expectedResult = service.addMeseToCollectionIfMissing(meseCollection, undefined, null);
          expect(expectedResult).toEqual(meseCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
