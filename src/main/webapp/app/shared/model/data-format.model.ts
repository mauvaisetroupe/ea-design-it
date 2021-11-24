export interface IDataFormat {
  id?: number;
  name?: string | null;
}

export class DataFormat implements IDataFormat {
  constructor(public id?: number, public name?: string | null) {}
}
