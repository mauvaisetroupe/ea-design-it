import { IFlowInterface } from '@/shared/model/flow-interface.model';
import axios from 'axios';

const baseApiUrl = 'api/reporting';

export default class ReportingService {
  public retrieveInterfaces(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl + '/duplicate-interface/')
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  //     @PostMapping(value = "reporting/merge-duplicate-interface/{id}")
  public mergeInterfaces(flowInterfaceVar: IFlowInterface): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/merge-duplicate-interface/${flowInterfaceVar.id}`, (flowInterfaceVar as any).mergeList)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
    return null;
  }
}
