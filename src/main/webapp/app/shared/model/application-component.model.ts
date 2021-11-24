import { IApplication } from '@/shared/model/application.model';
import { IApplicationCategory } from '@/shared/model/application-category.model';

import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';
export interface IApplicationComponent {
  id?: number;
  name?: string | null;
  description?: string | null;
  technology?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  applicationType?: ApplicationType | null;
  softwareType?: SoftwareType | null;
  application?: IApplication;
  category?: IApplicationCategory | null;
}

export class ApplicationComponent implements IApplicationComponent {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public technology?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public applicationType?: ApplicationType | null,
    public softwareType?: SoftwareType | null,
    public application?: IApplication,
    public category?: IApplicationCategory | null
  ) {}
}
