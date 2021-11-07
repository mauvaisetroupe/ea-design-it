import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IOwner } from '@/shared/model/owner.model';

import { ViewPoint } from '@/shared/model/enumerations/view-point.model';
export interface ILandscapeView {
  id?: number;
  viewpoint?: ViewPoint | null;
  diagramName?: string | null;
  flows?: IFunctionalFlow[] | null;
  owner?: IOwner | null;
}

export class LandscapeView implements ILandscapeView {
  constructor(
    public id?: number,
    public viewpoint?: ViewPoint | null,
    public diagramName?: string | null,
    public flows?: IFunctionalFlow[] | null,
    public owner?: IOwner | null
  ) {}
}
