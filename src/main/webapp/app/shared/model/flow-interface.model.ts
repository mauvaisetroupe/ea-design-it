import { IDataFlow } from '@/shared/model/data-flow.model';
import { IApplication } from '@/shared/model/application.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';
import { IOwner } from '@/shared/model/owner.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import { Protocol } from '@/shared/model/enumerations/protocol.model';
export interface IFlowInterface {
  id?: string;
  protocol?: Protocol | null;
  status?: string | null;
  dataFlows?: IDataFlow[] | null;
  source?: IApplication | null;
  target?: IApplication | null;
  sourceComponent?: IApplicationComponent | null;
  targetComponent?: IApplicationComponent | null;
  owner?: IOwner | null;
  functionalFlows?: IFunctionalFlow[] | null;
}

export class FlowInterface implements IFlowInterface {
  constructor(
    public id?: string,
    public protocol?: Protocol | null,
    public status?: string | null,
    public dataFlows?: IDataFlow[] | null,
    public source?: IApplication | null,
    public target?: IApplication | null,
    public sourceComponent?: IApplicationComponent | null,
    public targetComponent?: IApplicationComponent | null,
    public owner?: IOwner | null,
    public functionalFlows?: IFunctionalFlow[] | null
  ) {}
}
