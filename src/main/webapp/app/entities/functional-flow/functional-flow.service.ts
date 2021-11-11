import axios from 'axios';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

const baseApiUrl = 'api/functional-flows';
const basePlantUMLApiUrl = 'api/plantuml/functional-flow/get-image-with-media-type';

export default class FunctionalFlowService {
  public find(id: number): Promise<IFunctionalFlow> {
    return new Promise<IFunctionalFlow>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
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
        .get(baseApiUrl)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
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
        .post(`${baseApiUrl}`, entity)
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
        .put(`${baseApiUrl}/${entity.id}`, entity)
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
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUML(id: number) {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
