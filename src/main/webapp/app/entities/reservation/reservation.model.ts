import * as dayjs from 'dayjs';
import { IMese } from 'app/entities/mese/mese.model';

export interface IReservation {
  id?: number;
  scrumiera?: boolean | null;
  nrPers?: number | null;
  startDatetime?: dayjs.Dayjs | null;
  endDateime?: dayjs.Dayjs | null;
  mese?: IMese[] | null;
}

export class Reservation implements IReservation {
  constructor(
    public id?: number,
    public scrumiera?: boolean | null,
    public nrPers?: number | null,
    public startDatetime?: dayjs.Dayjs | null,
    public endDateime?: dayjs.Dayjs | null,
    public mese?: IMese[] | null
  ) {
    this.scrumiera = this.scrumiera ?? false;
  }
}

export function getReservationIdentifier(reservation: IReservation): number | undefined {
  return reservation.id;
}
