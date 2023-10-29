import { type ICapability } from './capability.model';

export interface ICapabilityImport {
  l0: ICapability;
  l1: ICapability;
  l2: ICapability;
  l3: ICapability;
  domain: ICapability;
  status?: string;
  error?: string;
}
