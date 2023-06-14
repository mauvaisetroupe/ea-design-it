import { ICapability } from './capability.model';
import { ICapabilityImport } from './capability-import.model';

export interface IApplicationCapabilityImport {
  sheetname: string;
  dtos: IApplicationCapabilityImportItem[];
}

export interface IApplicationCapabilityImportItem {
  applicationNames: String[];
  capabilityImportDTO: ICapabilityImport;
  importStatus: string;
  errorMessage: string;
}
