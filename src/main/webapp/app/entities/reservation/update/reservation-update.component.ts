import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReservation, Reservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';
import { IMese } from 'app/entities/mese/mese.model';
import { MeseService } from 'app/entities/mese/service/mese.service';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html',
})
export class ReservationUpdateComponent implements OnInit {
  isSaving = false;

  meseSharedCollection: IMese[] = [];

  editForm = this.fb.group({
    id: [],
    scrumiera: [],
    nrPers: [],
    startDatetime: [],
    endDateime: [],
    mese: [],
  });

  constructor(
    protected reservationService: ReservationService,
    protected meseService: MeseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      if (reservation.id === undefined) {
        const today = dayjs().startOf('day');
        reservation.startDatetime = today;
        reservation.endDateime = today;
      }

      this.updateForm(reservation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reservation = this.createFromForm();
    if (reservation.id !== undefined) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  trackMeseById(index: number, item: IMese): number {
    return item.id!;
  }

  getSelectedMese(option: IMese, selectedVals?: IMese[]): IMese {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reservation: IReservation): void {
    this.editForm.patchValue({
      id: reservation.id,
      scrumiera: reservation.scrumiera,
      nrPers: reservation.nrPers,
      startDatetime: reservation.startDatetime ? reservation.startDatetime.format(DATE_TIME_FORMAT) : null,
      endDateime: reservation.endDateime ? reservation.endDateime.format(DATE_TIME_FORMAT) : null,
      mese: reservation.mese,
    });

    this.meseSharedCollection = this.meseService.addMeseToCollectionIfMissing(this.meseSharedCollection, ...(reservation.mese ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.meseService
      .query()
      .pipe(map((res: HttpResponse<IMese[]>) => res.body ?? []))
      .pipe(map((mese: IMese[]) => this.meseService.addMeseToCollectionIfMissing(mese, ...(this.editForm.get('mese')!.value ?? []))))
      .subscribe((mese: IMese[]) => (this.meseSharedCollection = mese));
  }

  protected createFromForm(): IReservation {
    return {
      ...new Reservation(),
      id: this.editForm.get(['id'])!.value,
      scrumiera: this.editForm.get(['scrumiera'])!.value,
      nrPers: this.editForm.get(['nrPers'])!.value,
      startDatetime: this.editForm.get(['startDatetime'])!.value
        ? dayjs(this.editForm.get(['startDatetime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      endDateime: this.editForm.get(['endDateime'])!.value ? dayjs(this.editForm.get(['endDateime'])!.value, DATE_TIME_FORMAT) : undefined,
      mese: this.editForm.get(['mese'])!.value,
    };
  }
}
