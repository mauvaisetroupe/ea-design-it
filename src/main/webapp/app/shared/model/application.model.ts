import { type IOwner } from '@/shared/model/owner.model';
import { type IOrganizationalEntity } from '@/shared/model/organizational-entity.model';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import { type ITechnology } from '@/shared/model/technology.model';
import { type IExternalReference } from '@/shared/model/external-reference.model';
import { type IApplicationComponent } from '@/shared/model/application-component.model';
import { type ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';
import { type IDataObject } from '@/shared/model/data-object.model';

import { type ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { type SoftwareType } from '@/shared/model/enumerations/software-type.model';
export interface IApplication {
  id?: number;
  alias?: string | null;
  name?: string;
  description?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  applicationType?: keyof typeof ApplicationType | null;
  softwareType?: keyof typeof SoftwareType | null;
  nickname?: string | null;
  owner?: IOwner | null;
  itOwner?: IOwner | null;
  businessOwner?: IOwner | null;
  organizationalEntity?: IOrganizationalEntity | null;
  categories?: IApplicationCategory[] | null;
  technologies?: ITechnology[] | null;
  externalIDS?: IExternalReference[] | null;
  applicationsLists?: IApplicationComponent[] | null;
  capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null;
  dataObjects?: IDataObject[] | null;
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
    public applicationType?: keyof typeof ApplicationType | null,
    public softwareType?: keyof typeof SoftwareType | null,
    public nickname?: string | null,
    public owner?: IOwner | null,
    public itOwner?: IOwner | null,
    public businessOwner?: IOwner | null,
    public organizationalEntity?: IOrganizationalEntity | null,
    public categories?: IApplicationCategory[] | null,
    public technologies?: ITechnology[] | null,
    public externalIDS?: IExternalReference[] | null,
    public applicationsLists?: IApplicationComponent[] | null,
    public capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null,
    public dataObjects?: IDataObject[] | null,
  ) {}
}
