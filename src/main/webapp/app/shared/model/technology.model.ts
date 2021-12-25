import { IApplication } from '@/shared/model/application.model';
import { IApplicationComponent } from '@/shared/model/application-component.model';

export interface ITechnology {
  id?: number;
  name?: string;
  type?: string | null;
  description?: string | null;
  applications?: IApplication[] | null;
  components?: IApplicationComponent[] | null;
}

export class Technology implements ITechnology {
  constructor(
    public id?: number,
    public name?: string,
    public type?: string | null,
    public description?: string | null,
    public applications?: IApplication[] | null,
    public components?: IApplicationComponent[] | null
  ) {}
}
