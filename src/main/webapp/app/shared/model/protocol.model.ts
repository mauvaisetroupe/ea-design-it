import { ProtocolType } from '@/shared/model/enumerations/protocol-type.model';
export interface IProtocol {
  id?: number;
  name?: string;
  type?: ProtocolType;
  description?: string | null;
  scope?: string | null;
}

export class Protocol implements IProtocol {
  constructor(
    public id?: number,
    public name?: string,
    public type?: ProtocolType,
    public description?: string | null,
    public scope?: string | null
  ) {}
}
