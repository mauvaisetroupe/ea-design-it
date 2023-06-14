import { ICapability } from '@/shared/model/capability.model';
import { IApplication } from '@/shared/model/application.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';

export interface ICapabilityApplicationMapping {
  id?: number;
  capability?: ICapability | null;
  application?: IApplication | null;
  landscapes?: ILandscapeView[] | null;
}

export class CapabilityApplicationMapping implements ICapabilityApplicationMapping {
  constructor(
    public id?: number,
    public capability?: ICapability | null,
    public application?: IApplication | null,
    public landscapes?: ILandscapeView[] | null
  ) {}
}
