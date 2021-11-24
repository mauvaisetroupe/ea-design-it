export interface IApplicationCategory {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class ApplicationCategory implements IApplicationCategory {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}
