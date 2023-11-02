import { type IOwner } from '@/shared/model/owner.model';
import { type IApplication } from '@/shared/model/application.model';
import { type ITechnology } from '@/shared/model/technology.model';
import { type IBusinessObject } from '@/shared/model/business-object.model';

import { type DataObjectType } from '@/shared/model/enumerations/data-object-type.model';
export interface IDataObject {
  id?: number;
  name?: string;
  type?: keyof typeof DataObjectType | null;
  components?: IDataObject[] | null;
  owner?: IOwner | null;
  application?: IApplication | null;
  technologies?: ITechnology[] | null;
  businessObject?: IBusinessObject | null;
  container?: IDataObject | null;
}

export class DataObject implements IDataObject {
  constructor(
    public id?: number,
    public name?: string,
    public type?: keyof typeof DataObjectType | null,
    public components?: IDataObject[] | null,
    public owner?: IOwner | null,
    public application?: IApplication | null,
    public technologies?: ITechnology[] | null,
    public businessObject?: IBusinessObject | null,
    public container?: IDataObject | null,
  ) {}
}
