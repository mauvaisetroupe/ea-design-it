import { IApplication } from '@/shared/model/application.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
export interface IApplicationComponent {
  id?: number;
  name?: string | null;
  description?: string | null;
  type?: ApplicationType | null;
  technology?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  application?: IApplication;
}

export class ApplicationComponent implements IApplicationComponent {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public type?: ApplicationType | null,
    public technology?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public application?: IApplication
  ) {}
}
