import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMese, Mese } from '../mese.model';
import { MeseService } from '../service/mese.service';

@Component({
  selector: 'jhi-mese-update',
  templateUrl: './mese-update.component.html',
})
export class MeseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nrLoc: [],
    outdoor: [],
  });

  constructor(protected meseService: MeseService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mese }) => {
      this.updateForm(mese);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mese = this.createFromForm();
    if (mese.id !== undefined) {
      this.subscribeToSaveResponse(this.meseService.update(mese));
    } else {
      this.subscribeToSaveResponse(this.meseService.create(mese));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMese>>): void {
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

  protected updateForm(mese: IMese): void {
    this.editForm.patchValue({
      id: mese.id,
      nrLoc: mese.nrLoc,
      outdoor: mese.outdoor,
    });
  }

  protected createFromForm(): IMese {
    return {
      ...new Mese(),
      id: this.editForm.get(['id'])!.value,
      nrLoc: this.editForm.get(['nrLoc'])!.value,
      outdoor: this.editForm.get(['outdoor'])!.value,
    };
  }
}
