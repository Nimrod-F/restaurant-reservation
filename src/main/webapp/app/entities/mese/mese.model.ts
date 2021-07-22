import { IRestaurants } from 'app/entities/restaurants/restaurants.model';
import { IReservation } from 'app/entities/reservation/reservation.model';

export interface IMese {
  id?: number;
  nrLoc?: number | null;
  outdoor?: boolean | null;
  restaurants?: IRestaurants[] | null;
  reservations?: IReservation[] | null;
}

export class Mese implements IMese {
  constructor(
    public id?: number,
    public nrLoc?: number | null,
    public outdoor?: boolean | null,
    public restaurants?: IRestaurants[] | null,
    public reservations?: IReservation[] | null
  ) {
    this.outdoor = this.outdoor ?? false;
  }
}

export function getMeseIdentifier(mese: IMese): number | undefined {
  return mese.id;
}
