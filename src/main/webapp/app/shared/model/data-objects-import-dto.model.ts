import type { IDataObjectImport } from './data-objects-import.model';

export interface IDataObjectImportDTO {
  sheetname?: string;
  dtos?: IDataObjectImport[];
}

export class DataObjectImportDTO implements IDataObjectImportDTO {
  constructor(
    public sheetname?: string,
    public dtos?: IDataObjectImport[],
  ) {}
}
