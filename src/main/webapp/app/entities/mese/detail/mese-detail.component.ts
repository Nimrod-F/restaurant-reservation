import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMese } from '../mese.model';

@Component({
  selector: 'jhi-mese-detail',
  templateUrl: './mese-detail.component.html',
})
export class MeseDetailComponent implements OnInit {
  mese: IMese | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mese }) => {
      this.mese = mese;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
