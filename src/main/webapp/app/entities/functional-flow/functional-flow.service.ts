import axios from 'axios';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

const functionalFlowBaseApiUrl = 'api/functional-flows';
const functionalFlowPlantUMLApiUrl = 'api/plantuml/functional-flow';
const sequenceDiagramApiUrl = 'api/import/flow/sequence-diagram';
const baseApiUrl = 'api/plantuml/sequence-diagram';

export default class FunctionalFlowService {
  public find(id: number): Promise<IFunctionalFlow> {
    return new Promise<IFunctionalFlow>((resolve, reject) => {
      axios
        .get(`${functionalFlowBaseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(functionalFlowBaseApiUrl)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number, deleteFlowInterfaces: boolean, deleteDatas: boolean): Promise<any> {
    const params = { deleteFlowInterfaces: deleteFlowInterfaces, deleteDatas: deleteDatas };
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${functionalFlowBaseApiUrl}/${id}`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IFunctionalFlow): Promise<IFunctionalFlow> {
    return new Promise<IFunctionalFlow>((resolve, reject) => {
      axios
        .post(`${functionalFlowBaseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IFunctionalFlow): Promise<IFunctionalFlow> {
    return new Promise<IFunctionalFlow>((resolve, reject) => {
      axios
        .put(`${functionalFlowBaseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IFunctionalFlow): Promise<IFunctionalFlow> {
    return new Promise<IFunctionalFlow>((resolve, reject) => {
      axios
        .patch(`${functionalFlowBaseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUML(functionalFlowId: number, sequenceDiagram: boolean) {
    return new Promise<any>((resolve, reject) => {
      const params = { diagramType: sequenceDiagram ? 'SEQUENCE_DIAGRAM' : 'COMPONENT_DIAGRAM' };
      axios
        .get(`${functionalFlowPlantUMLApiUrl}/get-svg/${functionalFlowId}`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUMLSource(functionalFlowId: number, sequenceDiagram: boolean, preparedForEdition = false) {
    const params = { diagramType: sequenceDiagram ? 'SEQUENCE_DIAGRAM' : 'COMPONENT_DIAGRAM', preparedForEdition: preparedForEdition };
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${functionalFlowPlantUMLApiUrl}/get-source/${functionalFlowId}`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUMLFromString(source: string) {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/get-svg`, source.replace(/[\n\r]/g, '###CR##'))
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
        .post(`${sequenceDiagramApiUrl}/pre-import`, source.replace(/[\n\r]/g, '###CR##'))
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public saveImport(flowImport, landscapeId: number) {
    const params = { landscapeId: landscapeId };
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${sequenceDiagramApiUrl}/save`, flowImport, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
