import { IApplication } from '@/shared/model/application.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';
import { ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';

export interface ICapability {
  id?: number;
  name?: string;
  description?: string | null;
  comment?: string | null;
  level?: number | null;
  subCapabilities?: ICapability[] | null;
  parent?: ICapability | null;
  //  applications?: IApplication[] | null;
  //  landscapes?: ILandscapeView[] | null;
  inheritedApplications?: IApplication[] | null; // calculated
  capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null;
}

export class Capability implements ICapability {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public comment?: string | null,
    public level?: number | null,
    public subCapabilities?: ICapability[] | null,
    public parent?: ICapability | null,
    public capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null
  ) //    public applications?: IApplication[] | null,
  //    public landscapes?: ILandscapeView[] | null
  {}
}
