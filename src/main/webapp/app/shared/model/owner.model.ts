import { IUser } from '@/shared/model/user.model';

export interface IOwner {
  id?: number;
  name?: string;
  firstname?: string | null;
  lastname?: string | null;
  email?: string | null;
  users?: IUser[] | null;
}

export class Owner implements IOwner {
  constructor(
    public id?: number,
    public name?: string,
    public firstname?: string | null,
    public lastname?: string | null,
    public email?: string | null,
    public users?: IUser[] | null
  ) {}
}
