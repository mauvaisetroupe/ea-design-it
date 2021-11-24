import { IOwner } from '@/shared/model/owner.model';
import { IApplicationCategory } from '@/shared/model/application-category.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';
export interface IApplication {
  id?: number;
  alias?: string | null;
  name?: string | null;
  description?: string | null;
  technology?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  applicationType?: ApplicationType | null;
  softwareType?: SoftwareType | null;
  owner?: IOwner | null;
  category?: IApplicationCategory | null;
  applicationsLists?: IApplicationComponent[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public alias?: string | null,
    public name?: string | null,
    public description?: string | null,
    public technology?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public applicationType?: ApplicationType | null,
    public softwareType?: SoftwareType | null,
    public owner?: IOwner | null,
    public category?: IApplicationCategory | null,
    public applicationsLists?: IApplicationComponent[] | null
  ) {}
}
