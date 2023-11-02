import { type IDataObject } from '@/shared/model/data-object.model';
import { type IOwner } from '@/shared/model/owner.model';

export interface IBusinessObject {
  id?: number;
  name?: string;
  implementable?: boolean | null;
  spacilizations?: IBusinessObject[] | null;
  components?: IBusinessObject[] | null;
  dataObjects?: IDataObject[] | null;
  owner?: IOwner | null;
  generalization?: IBusinessObject | null;
  container?: IBusinessObject | null;
}

export class BusinessObject implements IBusinessObject {
  constructor(
    public id?: number,
    public name?: string,
    public implementable?: boolean | null,
    public spacilizations?: IBusinessObject[] | null,
    public components?: IBusinessObject[] | null,
    public dataObjects?: IDataObject[] | null,
    public owner?: IOwner | null,
    public generalization?: IBusinessObject | null,
    public container?: IBusinessObject | null,
  ) {
    this.implementable = this.implementable ?? false;
  }
}
