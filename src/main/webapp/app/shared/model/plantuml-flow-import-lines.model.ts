import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { IFlowGroup } from '@/shared/model/flow-group.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IApplication } from './application.model';
import { IProtocol } from './protocol.model';

export interface FlowImportLines {
  id?: number;
  description?: string | null;
  order?: number | null;
  source?: IApplication;
  target?: IApplication;
  selectedInterface?: IFlowInterface;
  interfaceAlias?: string | null;
  potentialInterfaces: IFlowInterface[];
  protocol: IProtocol;
}
