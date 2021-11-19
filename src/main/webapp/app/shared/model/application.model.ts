import { IOwner } from '@/shared/model/owner.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
export interface IApplication {
  id?: number;
  alias?: string | null;
  name?: string | null;
  description?: string | null;
  type?: ApplicationType | null;
  technology?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  owner?: IOwner | null;
  applicationsLists?: IApplicationComponent[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public alias?: string | null,
    public name?: string | null,
    public description?: string | null,
    public type?: ApplicationType | null,
    public technology?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public owner?: IOwner | null,
    public applicationsLists?: IApplicationComponent[] | null
  ) {}
}
