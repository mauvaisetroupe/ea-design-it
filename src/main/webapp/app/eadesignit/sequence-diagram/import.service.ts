import axios from 'axios';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

const basePlantUMLApiUrl = 'api/plantuml';
const importApiUrl = 'api/import/flow/sequence-diagram';

export default class SequenceDiagramService {
  public getPlantUMLFromString(source: string) {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${basePlantUMLApiUrl}/sequence-diagram/get-svg`, source.replace(/[\n\r]/g, '###CR##'))
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public importPlantuml(source: string) {
    return new Promise<any>((resolve, reject) => {
      console.log('importPlantuml source ');
      axios
        .post(`${importApiUrl}/pre-import`, source.replace(/[\n\r]/g, '###CR##'))
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public saveImport(flowImport, landscapeId) {
    const params = { landscapeId: landscapeId };
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${importApiUrl}/save`, flowImport, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
