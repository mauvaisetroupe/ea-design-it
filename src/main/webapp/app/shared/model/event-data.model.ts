import { IDataFlow } from '@/shared/model/data-flow.model';

export interface IEventData {
  id?: number;
  name?: string | null;
  contractURL?: string | null;
  documentationURL?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  dataFlow?: IDataFlow | null;
}

export class EventData implements IEventData {
  constructor(
    public id?: number,
    public name?: string | null,
    public contractURL?: string | null,
    public documentationURL?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public dataFlow?: IDataFlow | null
  ) {}
}
