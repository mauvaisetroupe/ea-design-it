import { IOwner } from '@/shared/model/owner.model';
import { IApplicationCategory } from '@/shared/model/application-category.model';
import { ITechnology } from '@/shared/model/technology.model';
import { ICapability } from '@/shared/model/capability.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';
export interface IApplication {
  id?: number;
  alias?: string | null;
  name?: string;
  description?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  applicationType?: ApplicationType | null;
  softwareType?: SoftwareType | null;
  owner?: IOwner | null;
  itOwner?: IOwner | null;
  businessOwner?: IOwner | null;
  categories?: IApplicationCategory[] | null;
  technologies?: ITechnology[] | null;
  capabilities?: ICapability[] | null;
  applicationsLists?: IApplicationComponent[] | null;
}

export class Application implements IApplication {
  constructor(
    public id?: number,
    public alias?: string | null,
    public name?: string,
    public description?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public applicationType?: ApplicationType | null,
    public softwareType?: SoftwareType | null,
    public owner?: IOwner | null,
    public itOwner?: IOwner | null,
    public businessOwner?: IOwner | null,
    public categories?: IApplicationCategory[] | null,
    public technologies?: ITechnology[] | null,
    public capabilities?: ICapability[] | null,
    public applicationsLists?: IApplicationComponent[] | null
  ) {}
}
