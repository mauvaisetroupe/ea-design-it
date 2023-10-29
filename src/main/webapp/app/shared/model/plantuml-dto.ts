import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IApplication } from './application.model';
import { type IProtocol } from './protocol.model';
import { type IApplicationComponent } from './application-component.model';

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
