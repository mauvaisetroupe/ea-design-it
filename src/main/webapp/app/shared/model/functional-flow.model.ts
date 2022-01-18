import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import { IOwner } from '@/shared/model/owner.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';
import { IDataFlow } from '@/shared/model/data-flow.model';

export interface IFunctionalFlow {
  id?: number;
  alias?: string;
  description?: string | null;
  comment?: string | null;
  status?: string | null;
  documentationURL?: string | null;
  documentationURL2?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  steps?: IFunctionalFlowStep[] | null;
  owner?: IOwner | null;
  landscapes?: ILandscapeView[] | null;
  dataFlows?: IDataFlow[] | null;
}

export class FunctionalFlow implements IFunctionalFlow {
  constructor(
    public id?: number,
    public alias?: string,
    public description?: string | null,
    public comment?: string | null,
    public status?: string | null,
    public documentationURL?: string | null,
    public documentationURL2?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public steps?: IFunctionalFlowStep[] | null,
    public owner?: IOwner | null,
    public landscapes?: ILandscapeView[] | null,
    public dataFlows?: IDataFlow[] | null
  ) {}
}
