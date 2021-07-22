import { IMese } from 'app/entities/mese/mese.model';

export interface IRestaurants {
  id?: number;
  location?: string | null;
  name?: string | null;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  mese?: IMese | null;
}

export class Restaurants implements IRestaurants {
  constructor(
    public id?: number,
    public location?: string | null,
    public name?: string | null,
    public description?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public mese?: IMese | null
  ) {}
}

export function getRestaurantsIdentifier(restaurants: IRestaurants): number | undefined {
  return restaurants.id;
}
