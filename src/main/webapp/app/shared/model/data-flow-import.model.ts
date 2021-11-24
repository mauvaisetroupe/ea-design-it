import { ImportStatus } from '@/shared/model/enumerations/import-status.model';
export interface IDataFlowImport {
  id?: number;
  dataResourceName?: string | null;
  dataResourceType?: string | null;
  dataDescription?: string | null;
  dataDocumentationURL?: string | null;
  dataItemResourceName?: string | null;
  dataItemResourceType?: string | null;
  dataItemDescription?: string | null;
  dataItemDocumentationURL?: string | null;
  frequency?: string | null;
  format?: string | null;
  contractURL?: string | null;
  importDataFlowStatus?: ImportStatus | null;
  importDataFlowItemStatus?: ImportStatus | null;
  importStatusMessage?: string | null;
}

export class DataFlowImport implements IDataFlowImport {
  constructor(
    public id?: number,
    public dataResourceName?: string | null,
    public dataResourceType?: string | null,
    public dataDescription?: string | null,
    public dataDocumentationURL?: string | null,
    public dataItemResourceName?: string | null,
    public dataItemResourceType?: string | null,
    public dataItemDescription?: string | null,
    public dataItemDocumentationURL?: string | null,
    public frequency?: string | null,
    public format?: string | null,
    public contractURL?: string | null,
    public importDataFlowStatus?: ImportStatus | null,
    public importDataFlowItemStatus?: ImportStatus | null,
    public importStatusMessage?: string | null
  ) {}
}
