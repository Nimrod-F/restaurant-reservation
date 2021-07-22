import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mese',
        data: { pageTitle: 'restaurantReservationApp.mese.home.title' },
        loadChildren: () => import('./mese/mese.module').then(m => m.MeseModule),
      },
      {
        path: 'reservation',
        data: { pageTitle: 'restaurantReservationApp.reservation.home.title' },
        loadChildren: () => import('./reservation/reservation.module').then(m => m.ReservationModule),
      },
      {
        path: 'restaurants',
        data: { pageTitle: 'restaurantReservationApp.restaurants.home.title' },
        loadChildren: () => import('./restaurants/restaurants.module').then(m => m.RestaurantsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
