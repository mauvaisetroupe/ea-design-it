import { ICapability } from './capability.model';
import { ICapabilityImport } from './capability-import.model';

export interface IApplicationCapabilityImport {
  sheetname?: string;
  dtos?: IApplicationCapabilityImportItem[];
}

export interface IApplicationCapabilityImportItem {
  applicationNames: string[];
  capabilityImportDTO: ICapabilityImport;
  importStatus: string;
  errorMessage: string;
}
