import axios from 'axios';

import { type IFlowInterface } from '@/shared/model/flow-interface.model';

const baseApiUrl = 'api/flow-interfaces';

export default class FlowInterfaceService {
  public find(id: number): Promise<IFlowInterface> {
    return new Promise<IFlowInterface>((resolve, reject) => {
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

  public search(sourceId: number, targetId: number, protocolId: number): Promise<any> {
    let search = 'sourceId:' + sourceId + ',targetId:' + targetId;
    if (protocolId) search = search + ',protocolId:' + protocolId;
    const params = { search: search };
    return new Promise<any>((resolve, reject) => {
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

  public create(entity: IFlowInterface): Promise<IFlowInterface> {
    return new Promise<IFlowInterface>((resolve, reject) => {
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

  public update(entity: IFlowInterface): Promise<IFlowInterface> {
    return new Promise<IFlowInterface>((resolve, reject) => {
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

  public partialUpdate(entity: IFlowInterface): Promise<IFlowInterface> {
    return new Promise<IFlowInterface>((resolve, reject) => {
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
}
