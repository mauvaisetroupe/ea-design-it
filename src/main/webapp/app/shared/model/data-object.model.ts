import { type IApplication } from '@/shared/model/application.model';
import { type IOwner } from '@/shared/model/owner.model';
import { type ITechnology } from '@/shared/model/technology.model';
import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import { type IBusinessObject } from '@/shared/model/business-object.model';

import { type DataObjectType } from '@/shared/model/enumerations/data-object-type.model';
export interface IDataObject {
  id?: number;
  name?: string;
  type?: keyof typeof DataObjectType | null;
  components?: IDataObject[] | null;
  application?: IApplication | null;
  owner?: IOwner | null;
  technologies?: ITechnology[] | null;
  landscapes?: ILandscapeView[] | null;
  parent?: IDataObject | null;
  businessObject?: IBusinessObject | null;
}

export class DataObject implements IDataObject {
  constructor(
    public id?: number,
    public name?: string,
    public type?: keyof typeof DataObjectType | null,
    public components?: IDataObject[] | null,
    public application?: IApplication | null,
    public owner?: IOwner | null,
    public technologies?: ITechnology[] | null,
    public landscapes?: ILandscapeView[] | null,
    public parent?: IDataObject | null,
    public businessObject?: IBusinessObject | null,
  ) {}
}
