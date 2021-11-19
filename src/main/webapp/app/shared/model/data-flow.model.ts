import { IEventData } from '@/shared/model/event-data.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import { Frequency } from '@/shared/model/enumerations/frequency.model';
import { Format } from '@/shared/model/enumerations/format.model';
import { FlowType } from '@/shared/model/enumerations/flow-type.model';
export interface IDataFlow {
  id?: number;
  frequency?: Frequency | null;
  format?: Format | null;
  type?: FlowType | null;
  description?: string | null;
  resourceName?: string | null;
  contractURL?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  events?: IEventData[] | null;
  functionalFlows?: IFunctionalFlow[];
  flowInterface?: IFlowInterface;
}

export class DataFlow implements IDataFlow {
  constructor(
    public id?: number,
    public frequency?: Frequency | null,
    public format?: Format | null,
    public type?: FlowType | null,
    public description?: string | null,
    public resourceName?: string | null,
    public contractURL?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public events?: IEventData[] | null,
    public functionalFlows?: IFunctionalFlow[],
    public flowInterface?: IFlowInterface
  ) {}
}
