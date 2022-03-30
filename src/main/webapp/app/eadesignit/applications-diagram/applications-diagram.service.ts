import axios from 'axios';

import { IFlowInterface } from '@/shared/model/flow-interface.model';

const baseApiUrl = 'api/functional-flows';
const basePlantUMLApiUrl2 = 'api/plantuml/applications';

export default class applicationsDiagramService {
  public getPlantUMLforApplications(id: number[]) {
    const params = { ids: id };
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl2}/get-svg`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUMSourceforApplications(id: number[]) {
    const params = { ids: id };
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl2}/get-source`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public createNewFromApplications(applicationIds: number[]): Promise<IFlowInterface[]> {
    const params = { ids: applicationIds };
    return new Promise<IFlowInterface[]>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/new/applications`, { params })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
