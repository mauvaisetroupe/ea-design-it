import { IDataFlow } from '@/shared/model/data-flow.model';
import { IApplication } from '@/shared/model/application.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';
import { IProtocol } from '@/shared/model/protocol.model';
import { IOwner } from '@/shared/model/owner.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

export interface IFlowInterface {
  id?: number;
  alias?: string;
  status?: string | null;
  documentationURL?: string | null;
  documentationURL2?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  dataFlows?: IDataFlow[] | null;
  source?: IApplication;
  target?: IApplication;
  sourceComponent?: IApplicationComponent | null;
  targetComponent?: IApplicationComponent | null;
  protocol?: IProtocol | null;
  owner?: IOwner | null;
  functionalFlows?: IFunctionalFlow[] | null;
}

export class FlowInterface implements IFlowInterface {
  constructor(
    public id?: number,
    public alias?: string,
    public status?: string | null,
    public documentationURL?: string | null,
    public documentationURL2?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public dataFlows?: IDataFlow[] | null,
    public source?: IApplication,
    public target?: IApplication,
    public sourceComponent?: IApplicationComponent | null,
    public targetComponent?: IApplicationComponent | null,
    public protocol?: IProtocol | null,
    public owner?: IOwner | null,
    public functionalFlows?: IFunctionalFlow[] | null
  ) {}
}
