import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';
import { IDataFlow } from '@/shared/model/data-flow.model';

export interface IFunctionalFlow {
  id?: string;
  alias?: string | null;
  description?: string | null;
  comment?: string | null;
  status?: string | null;
  interfaces?: IFlowInterface[] | null;
  landscape?: ILandscapeView;
  dataFlows?: IDataFlow[] | null;
}

export class FunctionalFlow implements IFunctionalFlow {
  constructor(
    public id?: string,
    public alias?: string | null,
    public description?: string | null,
    public comment?: string | null,
    public status?: string | null,
    public interfaces?: IFlowInterface[] | null,
    public landscape?: ILandscapeView,
    public dataFlows?: IDataFlow[] | null
  ) {}
}
