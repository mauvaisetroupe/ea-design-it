import { type IApplication } from '@/shared/model/application.model';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import { type ITechnology } from '@/shared/model/technology.model';
import { type IExternalReference } from '@/shared/model/external-reference.model';

import { type ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { type SoftwareType } from '@/shared/model/enumerations/software-type.model';
export interface IApplicationComponent {
  id?: number;
  alias?: string | null;
  name?: string;
  description?: string | null;
  comment?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  applicationType?: keyof typeof ApplicationType | null;
  softwareType?: keyof typeof SoftwareType | null;
  displayInLandscape?: boolean | null;
  application?: IApplication;
  categories?: IApplicationCategory[] | null;
  technologies?: ITechnology[] | null;
  externalIDS?: IExternalReference[] | null;
}

export class ApplicationComponent implements IApplicationComponent {
  constructor(
    public id?: number,
    public alias?: string | null,
    public name?: string,
    public description?: string | null,
    public comment?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public applicationType?: keyof typeof ApplicationType | null,
    public softwareType?: keyof typeof SoftwareType | null,
    public displayInLandscape?: boolean | null,
    public application?: IApplication,
    public categories?: IApplicationCategory[] | null,
    public technologies?: ITechnology[] | null,
    public externalIDS?: IExternalReference[] | null,
  ) {
    this.displayInLandscape = this.displayInLandscape ?? false;
  }
}
