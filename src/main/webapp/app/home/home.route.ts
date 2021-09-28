import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import {RestaurantsModule} from "../entities/restaurants/restaurants.module";

export const HOME_ROUTE: Route = {
  path: '',
  data: { pageTitle: 'restaurantReservationApp.restaurants.home.title' },
  loadChildren: () => RestaurantsModule,
};
