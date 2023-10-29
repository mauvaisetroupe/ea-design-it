import { type ImportStatus } from '@/shared/model/enumerations/import-status.model';
export interface IDataFlowImport {
  id?: number;
  dataId?: string | null;
  dataParentId?: string | null;
  dataParentName?: string | null;
  functionalFlowId?: string | null;
  flowInterfaceId?: string | null;
  dataType?: string | null;
  dataResourceName?: string | null;
  dataResourceType?: string | null;
  dataDescription?: string | null;
  dataFrequency?: string | null;
  dataFormat?: string | null;
  dataContractURL?: string | null;
  dataDocumentationURL?: string | null;
  source?: string | null;
  target?: string | null;
  importDataStatus?: ImportStatus | null;
  importDataItemStatus?: ImportStatus | null;
  importStatusMessage?: string | null;
}

export class DataFlowImport implements IDataFlowImport {
  constructor(
    public id?: number,
    public dataId?: string | null,
    public dataParentId?: string | null,
    public dataParentName?: string | null,
    public functionalFlowId?: string | null,
    public flowInterfaceId?: string | null,
    public dataType?: string | null,
    public dataResourceName?: string | null,
    public dataResourceType?: string | null,
    public dataDescription?: string | null,
    public dataFrequency?: string | null,
    public dataFormat?: string | null,
    public dataContractURL?: string | null,
    public dataDocumentationURL?: string | null,
    public source?: string | null,
    public target?: string | null,
    public importDataStatus?: ImportStatus | null,
    public importDataItemStatus?: ImportStatus | null,
    public importStatusMessage?: string | null,
  ) {}
}
