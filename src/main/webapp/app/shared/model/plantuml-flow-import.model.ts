import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import { IOwner } from '@/shared/model/owner.model';
import { ILandscapeView } from '@/shared/model/landscape-view.model';
import { FlowImportLines } from './plantuml-flow-import-lines.model';

export interface IPlantumlFlowImport {
  id?: number;
  alias?: string | null;
  description?: string | null;
  comment?: string | null;
  status?: string | null;
  documentationURL?: string | null;
  documentationURL2?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  flowImportLines?: FlowImportLines[] | null;
  owner?: IOwner | null;
  onError?: boolean | null;
}
