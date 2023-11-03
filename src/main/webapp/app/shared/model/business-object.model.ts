import { type IDataObject } from '@/shared/model/data-object.model';
import { type IOwner } from '@/shared/model/owner.model';

export interface IBusinessObject {
  id?: number;
  name?: string;
  abstractBusinessObject?: boolean | null;
  specializations?: IBusinessObject[] | null;
  components?: IBusinessObject[] | null;
  dataObjects?: IDataObject[] | null;
  owner?: IOwner | null;
  generalization?: IBusinessObject | null;
  parent?: IBusinessObject | null;
}

export class BusinessObject implements IBusinessObject {
  constructor(
    public id?: number,
    public name?: string,
    public abstractBusinessObject?: boolean | null,
    public specializations?: IBusinessObject[] | null,
    public components?: IBusinessObject[] | null,
    public dataObjects?: IDataObject[] | null,
    public owner?: IOwner | null,
    public generalization?: IBusinessObject | null,
    public parent?: IBusinessObject | null,
  ) {
    this.abstractBusinessObject = this.abstractBusinessObject ?? false;
  }
}
