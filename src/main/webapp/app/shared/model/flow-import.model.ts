import { ImportStatus } from '@/shared/model/enumerations/import-status.model';
export interface IFlowImport {
  id?: number;
  idFlowFromExcel?: string | null;
  flowAlias?: string | null;
  sourceElement?: string | null;
  targetElement?: string | null;
  description?: string | null;
  integrationPattern?: string | null;
  frequency?: string | null;
  format?: string | null;
  swagger?: string | null;
  blueprint?: string | null;
  blueprintStatus?: string | null;
  flowStatus?: string | null;
  comment?: string | null;
  documentName?: string | null;
  importInterfaceStatus?: ImportStatus | null;
  importFunctionalFlowStatus?: ImportStatus | null;
  importDataFlowStatus?: ImportStatus | null;
  importStatusMessage?: string | null;
  existingApplicationID?: string | null;
}

export class FlowImport implements IFlowImport {
  constructor(
    public id?: number,
    public idFlowFromExcel?: string | null,
    public flowAlias?: string | null,
    public sourceElement?: string | null,
    public targetElement?: string | null,
    public description?: string | null,
    public integrationPattern?: string | null,
    public frequency?: string | null,
    public format?: string | null,
    public swagger?: string | null,
    public blueprint?: string | null,
    public blueprintStatus?: string | null,
    public flowStatus?: string | null,
    public comment?: string | null,
    public documentName?: string | null,
    public importInterfaceStatus?: ImportStatus | null,
    public importFunctionalFlowStatus?: ImportStatus | null,
    public importDataFlowStatus?: ImportStatus | null,
    public importStatusMessage?: string | null,
    public existingApplicationID?: string | null
  ) {}
}
