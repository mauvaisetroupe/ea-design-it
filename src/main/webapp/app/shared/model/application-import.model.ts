import { ImportStatus } from '@/shared/model/enumerations/import-status.model';
export interface IApplicationImport {
  id?: number;
  importId?: string | null;
  excelFileName?: string | null;
  idFromExcel?: string | null;
  name?: string | null;
  description?: string | null;
  type?: string | null;
  technology?: string | null;
  comment?: string | null;
  importStatus?: ImportStatus | null;
  importStatusMessage?: string | null;
  existingApplicationID?: string | null;
}

export class ApplicationImport implements IApplicationImport {
  constructor(
    public id?: number,
    public importId?: string | null,
    public excelFileName?: string | null,
    public idFromExcel?: string | null,
    public name?: string | null,
    public description?: string | null,
    public type?: string | null,
    public technology?: string | null,
    public comment?: string | null,
    public importStatus?: ImportStatus | null,
    public importStatusMessage?: string | null,
    public existingApplicationID?: string | null
  ) {}
}
