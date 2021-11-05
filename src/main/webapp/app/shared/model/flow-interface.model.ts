import { IDataFlow } from '@/shared/model/data-flow.model';
import { IApplication } from '@/shared/model/application.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';
import { IOwner } from '@/shared/model/owner.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import { Protocol } from '@/shared/model/enumerations/protocol.model';
export interface IFlowInterface {
  id?: number;
  protocol?: Protocol | null;
  status?: string | null;
  dataFlows?: IDataFlow[] | null;
  source?: IApplication;
  target?: IApplication;
  sourceComponent?: IApplicationComponent | null;
  targetComponent?: IApplicationComponent | null;
  owner?: IOwner;
  functionalFlows?: IFunctionalFlow[] | null;
}

export class FlowInterface implements IFlowInterface {
  constructor(
    public id?: number,
    public protocol?: Protocol | null,
    public status?: string | null,
    public dataFlows?: IDataFlow[] | null,
    public source?: IApplication,
    public target?: IApplication,
    public sourceComponent?: IApplicationComponent | null,
    public targetComponent?: IApplicationComponent | null,
    public owner?: IOwner,
    public functionalFlows?: IFunctionalFlow[] | null
  ) {}
}
