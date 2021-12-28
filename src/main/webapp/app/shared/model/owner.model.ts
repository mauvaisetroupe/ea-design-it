import { IUser } from '@/shared/model/user.model';

export interface IOwner {
  id?: number;
  name?: string | null;
  users?: IUser[] | null;
}

export class Owner implements IOwner {
  constructor(public id?: number, public name?: string | null, public users?: IUser[] | null) {}
}
