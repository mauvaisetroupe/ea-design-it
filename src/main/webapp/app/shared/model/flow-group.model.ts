import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

export interface IFlowGroup {
  id?: number;
  title?: string | null;
  url?: string | null;
  description?: string | null;
  flow?: IFunctionalFlow | null;
  steps?: IFunctionalFlowStep[];
}

export class FlowGroup implements IFlowGroup {
  constructor(
    public id?: number,
    public title?: string | null,
    public url?: string | null,
    public description?: string | null,
    public flow?: IFunctionalFlow | null,
    public steps?: IFunctionalFlowStep[],
  ) {}
}
