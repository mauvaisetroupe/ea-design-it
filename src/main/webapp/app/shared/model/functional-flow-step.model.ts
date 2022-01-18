import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

export interface IFunctionalFlowStep {
  id?: number;
  description?: string | null;
  flowInterface?: IFlowInterface;
  flow?: IFunctionalFlow;
}

export class FunctionalFlowStep implements IFunctionalFlowStep {
  constructor(
    public id?: number,
    public description?: string | null,
    public flowInterface?: IFlowInterface,
    public flow?: IFunctionalFlow
  ) {}
}
