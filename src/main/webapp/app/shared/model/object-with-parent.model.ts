export interface IObjectWithParent {
  parent?: IObjectWithParent | null;
  name: string;
  id: number;
  generalization?: IObjectWithParent | null;
}
