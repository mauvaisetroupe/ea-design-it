import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { IFlowGroup } from '@/shared/model/flow-group.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

export interface IFunctionalFlowStep {
  id?: number;
  description?: string | null;
  stepOrder?: number | null;
  flowInterface?: IFlowInterface;
  group?: IFlowGroup | null;
  flow?: IFunctionalFlow;
}

export class FunctionalFlowStep implements IFunctionalFlowStep {
  constructor(
    public id?: number,
    public description?: string | null,
    public stepOrder?: number | null,
    public flowInterface?: IFlowInterface,
    public group?: IFlowGroup | null,
    public flow?: IFunctionalFlow
  ) {}
}
