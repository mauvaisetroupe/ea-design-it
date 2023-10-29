import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { type IApplication } from './application.model';
import { type IProtocol } from './protocol.model';

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
  groupOrder?: number | null;
  groupFlowAlias?: string;
}
