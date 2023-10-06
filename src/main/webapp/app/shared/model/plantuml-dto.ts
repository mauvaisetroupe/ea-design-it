import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { IFlowGroup } from '@/shared/model/flow-group.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IApplication } from './application.model';
import { IProtocol } from './protocol.model';
import { IApplicationComponent } from './application-component.model';

export interface PlantumlDTO {
  svg: string;
  interfaces: IFLowInterfaceLight[];
  flows: IFunctionalFlow[];
  labelsShown: boolean;
}

export interface IFLowInterfaceLight {
  id?: number;
  alias?: string;
  description?: string | null;
  source?: IApplication;
  target?: IApplication;
  sourceComponent?: IApplicationComponent | null;
  targetComponent?: IApplicationComponent | null;
  protocol?: IProtocol | null;
}
