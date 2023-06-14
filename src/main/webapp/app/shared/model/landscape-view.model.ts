import { IOwner } from '@/shared/model/owner.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';

import { ViewPoint } from '@/shared/model/enumerations/view-point.model';
export interface ILandscapeView {
  id?: number;
  viewpoint?: ViewPoint | null;
  diagramName?: string | null;
  compressedDrawXML?: string | null;
  compressedDrawSVG?: string | null;
  owner?: IOwner | null;
  flows?: IFunctionalFlow[] | null;
  capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null;
}

export class LandscapeView implements ILandscapeView {
  constructor(
    public id?: number,
    public viewpoint?: ViewPoint | null,
    public diagramName?: string | null,
    public compressedDrawXML?: string | null,
    public compressedDrawSVG?: string | null,
    public owner?: IOwner | null,
    public flows?: IFunctionalFlow[] | null,
    public capabilityApplicationMappings?: ICapabilityApplicationMapping[] | null
  ) {}
}
