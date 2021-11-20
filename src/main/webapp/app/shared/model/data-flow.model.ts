import { IDataFlowItem } from '@/shared/model/data-flow-item.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import { Frequency } from '@/shared/model/enumerations/frequency.model';
import { Format } from '@/shared/model/enumerations/format.model';
export interface IDataFlow {
  id?: number;
  resourceName?: string | null;
  description?: string | null;
  frequency?: Frequency | null;
  format?: Format | null;
  contractURL?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  items?: IDataFlowItem[] | null;
  functionalFlows?: IFunctionalFlow[];
  flowInterface?: IFlowInterface;
}

export class DataFlow implements IDataFlow {
  constructor(
    public id?: number,
    public resourceName?: string | null,
    public description?: string | null,
    public frequency?: Frequency | null,
    public format?: Format | null,
    public contractURL?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public items?: IDataFlowItem[] | null,
    public functionalFlows?: IFunctionalFlow[],
    public flowInterface?: IFlowInterface
  ) {}
}
