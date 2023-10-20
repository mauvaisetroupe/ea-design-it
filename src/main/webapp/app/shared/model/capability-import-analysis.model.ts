import { ICapability } from './capability.model';

export interface ICapabilityImportAnalysisDTO {
  capabilitiesToAdd?: ICapabilityActionDTO[];
  capabilitiesToDelete?: ICapabilityActionDTO[];
  capabilitiesToDeleteWithMappings?: ICapabilityActionDTO[];
  ancestorsOfCapabilitiesWithMappings?: ICapabilityActionDTO[];
  errorLines?: string[];
}

export interface ICapabilityActionDTO {
  capability: ICapability;
  action: Action;
}


export enum Action {
  ADD = 'ADD',
  DELETE = 'DELETE',
  FORCE_DELETE = 'FORCE_DELETE',
  MOVE = 'MOVE',
  IGNORE = 'IGNORE',
}



