import { ProtocolType } from '@/shared/model/enumerations/protocol-type.model';
export interface IProtocol {
  id?: number;
  name?: string | null;
  type?: ProtocolType | null;
  description?: string | null;
  scope?: string | null;
}

export class Protocol implements IProtocol {
  constructor(
    public id?: number,
    public name?: string | null,
    public type?: ProtocolType | null,
    public description?: string | null,
    public scope?: string | null
  ) {}
}
