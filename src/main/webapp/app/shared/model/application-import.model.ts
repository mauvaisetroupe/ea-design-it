import { ImportStatus } from '@/shared/model/enumerations/import-status.model';
export interface IApplicationImport {
  id?: number;
  importId?: string | null;
  excelFileName?: string | null;
  idFromExcel?: string | null;
  name?: string | null;
  description?: string | null;
  type?: string | null;
  softwareType?: string | null;
  category1?: string | null;
  category2?: string | null;
  category3?: string | null;
  technology?: string | null;
  documentation?: string | null;
  comment?: string | null;
  owner?: string | null;
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
    public softwareType?: string | null,
    public category1?: string | null,
    public category2?: string | null,
    public category3?: string | null,
    public technology?: string | null,
    public documentation?: string | null,
    public comment?: string | null,
    public owner?: string | null,
    public importStatus?: ImportStatus | null,
    public importStatusMessage?: string | null,
    public existingApplicationID?: string | null
  ) {}
}
