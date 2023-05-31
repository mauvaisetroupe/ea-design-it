import { IExternalSystem } from '@/shared/model/external-system.model';
import { IApplication } from '@/shared/model/application.model';

export interface IExternalReference {
  id?: number;
  externalID?: string | null;
  externalSystem?: IExternalSystem | null;
  applications?: IApplication[] | null;
}

export class ExternalReference implements IExternalReference {
  constructor(
    public id?: number,
    public externalID?: string | null,
    public externalSystem?: IExternalSystem | null,
    public applications?: IApplication[] | null
  ) {}
}
