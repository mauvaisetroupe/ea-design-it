import { type IApplication } from '@/shared/model/application.model';

export interface IOrganizationalEntity {
  id?: number;
  name?: string;
  applications?: IApplication[] | null;
}

export class OrganizationalEntity implements IOrganizationalEntity {
  constructor(
    public id?: number,
    public name?: string,
    public applications?: IApplication[] | null,
  ) {}
}
