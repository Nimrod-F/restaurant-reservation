import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurants, Restaurants } from '../restaurants.model';
import { RestaurantsService } from '../service/restaurants.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMese } from 'app/entities/mese/mese.model';
import { MeseService } from 'app/entities/mese/service/mese.service';

@Component({
  selector: 'jhi-restaurants-update',
  templateUrl: './restaurants-update.component.html',
})
export class RestaurantsUpdateComponent implements OnInit {
  isSaving = false;

  meseSharedCollection: IMese[] = [];

  editForm = this.fb.group({
    id: [],
    location: [],
    name: [],
    description: [],
    image: [],
    imageContentType: [],
    mese: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected restaurantsService: RestaurantsService,
    protected meseService: MeseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurants }) => {
      this.updateForm(restaurants);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('restaurantReservationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurants = this.createFromForm();
    if (restaurants.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantsService.update(restaurants));
    } else {
      this.subscribeToSaveResponse(this.restaurantsService.create(restaurants));
    }
  }

  trackMeseById(index: number, item: IMese): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurants>>): void {
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

  protected updateForm(restaurants: IRestaurants): void {
    this.editForm.patchValue({
      id: restaurants.id,
      location: restaurants.location,
      name: restaurants.name,
      description: restaurants.description,
      image: restaurants.image,
      imageContentType: restaurants.imageContentType,
      mese: restaurants.mese,
    });

    this.meseSharedCollection = this.meseService.addMeseToCollectionIfMissing(this.meseSharedCollection, restaurants.mese);
  }

  protected loadRelationshipsOptions(): void {
    this.meseService
      .query()
      .pipe(map((res: HttpResponse<IMese[]>) => res.body ?? []))
      .pipe(map((mese: IMese[]) => this.meseService.addMeseToCollectionIfMissing(mese, this.editForm.get('mese')!.value)))
      .subscribe((mese: IMese[]) => (this.meseSharedCollection = mese));
  }

  protected createFromForm(): IRestaurants {
    return {
      ...new Restaurants(),
      id: this.editForm.get(['id'])!.value,
      location: this.editForm.get(['location'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      mese: this.editForm.get(['mese'])!.value,
    };
  }
}
