import axios from 'axios';

import { type IDataObject } from '@/shared/model/data-object.model';

const baseApiUrl = 'api/data-objects';
const basePlantUMLApiUrl = 'api/plantuml/data-object/get-svg';

export default class DataObjectService {
  public find(id: number): Promise<IDataObject> {
    return new Promise<IDataObject>((resolve, reject) => {
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

  public retrieve(eagerload: boolean): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const params = { eagerload: eagerload };
      axios
        .get(baseApiUrl, { params })
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

  public create(entity: IDataObject): Promise<IDataObject> {
    return new Promise<IDataObject>((resolve, reject) => {
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

  public update(entity: IDataObject): Promise<IDataObject> {
    return new Promise<IDataObject>((resolve, reject) => {
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

  public partialUpdate(entity: IDataObject): Promise<IDataObject> {
    return new Promise<IDataObject>((resolve, reject) => {
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

  public getPlantUML(id: number): string | PromiseLike<string> {
    return new Promise<string>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
