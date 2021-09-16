import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaurants } from '../restaurants.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-restaurants-detail',
  templateUrl: './restaurants-detail.component.html',
  styleUrls: ['./restaurants-detail.component.scss'],
})
export class RestaurantsDetailComponent implements OnInit {
  restaurants: IRestaurants | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurants }) => {
      this.restaurants = restaurants;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  reserveTable(): void {
    // TODO
  }

  convertImage(blob: any) {
    return `data:image/png;base64,${blob}`;
  }
}
