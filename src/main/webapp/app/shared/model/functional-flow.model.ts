import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';
import { IDataFlow } from '@/shared/model/data-flow.model';

export interface IFunctionalFlow {
  id?: number;
  alias?: string;
  description?: string | null;
  comment?: string | null;
  status?: string | null;
  documentationURL?: string | null;
  documentationURL2?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  interfaces?: IFlowInterface[] | null;
  landscapes?: ILandscapeView[] | null;
  dataFlows?: IDataFlow[] | null;
}

export class FunctionalFlow implements IFunctionalFlow {
  constructor(
    public id?: number,
    public alias?: string,
    public description?: string | null,
    public comment?: string | null,
    public status?: string | null,
    public documentationURL?: string | null,
    public documentationURL2?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public interfaces?: IFlowInterface[] | null,
    public landscapes?: ILandscapeView[] | null,
    public dataFlows?: IDataFlow[] | null
  ) {}
}
