export interface IExternalSystem {
  id?: number;
  externalSystemID?: string | null;
}

export class ExternalSystem implements IExternalSystem {
  constructor(
    public id?: number,
    public externalSystemID?: string | null,
  ) {}
}
