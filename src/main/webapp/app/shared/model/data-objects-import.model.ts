export interface IDataObjectImport {
  businessobject?: string;
  generalization?: string;
  abstractValue?: boolean;
  dataobject?: string;
  application?: string;
  landscapes?: string[];
}

export class DataObjectImport implements IDataObjectImport {
  constructor(
    public businessobject?: string,
    public generalization?: string,
    public abstractValue?: boolean,
    public dataobject?: string,
    public application?: string,
    public landscapes?: string[],
  ) {}
}
