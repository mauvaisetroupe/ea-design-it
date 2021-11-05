import { IOwner } from '@/shared/model/owner.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
export interface IApplication {
  id?: string;
  name?: string | null;
  description?: string | null;
  type?: ApplicationType | null;
  technology?: string | null;
  comment?: string | null;
  owner?: IOwner | null;
  applicationsLists?: IApplicationComponent[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: string,
    public name?: string | null,
    public description?: string | null,
    public type?: ApplicationType | null,
    public technology?: string | null,
    public comment?: string | null,
    public owner?: IOwner | null,
    public applicationsLists?: IApplicationComponent[] | null
  ) {}
}
