import { type IDataFlowItem } from '@/shared/model/data-flow-item.model';
import { type IDataFormat } from '@/shared/model/data-format.model';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';

import { type Frequency } from '@/shared/model/enumerations/frequency.model';
export interface IDataFlow {
  id?: number;
  resourceName?: string;
  resourceType?: string | null;
  description?: string | null;
  frequency?: keyof typeof Frequency | null;
  contractURL?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  items?: IDataFlowItem[] | null;
  format?: IDataFormat | null;
  functionalFlows?: IFunctionalFlow[] | null;
  flowInterface?: IFlowInterface | null;
}

export class DataFlow implements IDataFlow {
  constructor(
    public id?: number,
    public resourceName?: string,
    public resourceType?: string | null,
    public description?: string | null,
    public frequency?: keyof typeof Frequency | null,
    public contractURL?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public items?: IDataFlowItem[] | null,
    public format?: IDataFormat | null,
    public functionalFlows?: IFunctionalFlow[] | null,
    public flowInterface?: IFlowInterface | null,
  ) {}
}
