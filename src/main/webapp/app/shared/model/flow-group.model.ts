import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

export interface IFlowGroup {
  id?: number;
  title?: string | null;
  url?: string | null;
  flow?: IFunctionalFlow;
  steps?: IFunctionalFlowStep[];
}

export class FlowGroup implements IFlowGroup {
  constructor(
    public id?: number,
    public title?: string | null,
    public url?: string | null,
    public flow?: IFunctionalFlow,
    public steps?: IFunctionalFlowStep[]
  ) {}
}
