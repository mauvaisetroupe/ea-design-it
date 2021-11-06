export interface IApplicationImport {
  id?: number;
  importId?: Date | null;
  excelFileName?: string | null;
  idFromExcel?: string | null;
  name?: string | null;
  description?: string | null;
  type?: string | null;
  technology?: string | null;
  comment?: string | null;
}

export class ApplicationImport implements IApplicationImport {
  constructor(
    public id?: number,
    public importId?: Date | null,
    public excelFileName?: string | null,
    public idFromExcel?: string | null,
    public name?: string | null,
    public description?: string | null,
    public type?: string | null,
    public technology?: string | null,
    public comment?: string | null
  ) {}
}
