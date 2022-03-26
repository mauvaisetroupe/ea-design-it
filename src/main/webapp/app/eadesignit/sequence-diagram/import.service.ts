import axios from 'axios';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

const baseApiUrl = 'api/functional-flows';
const basePlantUMLApiUrl = 'api/plantuml/';
const importApiUrl = 'api/import/flow/sequence-diagram';

export default class SequenceDiagramService {
  public getPlantUML(source: String) {
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

  public importPlantuml(source: String) {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${importApiUrl}/`, source.replace(/[\n\r]/g, '###CR##'))
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
