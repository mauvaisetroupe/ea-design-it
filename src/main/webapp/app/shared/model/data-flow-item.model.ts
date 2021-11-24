import { IDataFlow } from '@/shared/model/data-flow.model';

export interface IDataFlowItem {
  id?: number;
  resourceName?: string | null;
  resourceType?: string | null;
  description?: string | null;
  contractURL?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  dataFlow?: IDataFlow | null;
}

export class DataFlowItem implements IDataFlowItem {
  constructor(
    public id?: number,
    public resourceName?: string | null,
    public resourceType?: string | null,
    public description?: string | null,
    public contractURL?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public dataFlow?: IDataFlow | null
  ) {}
}
