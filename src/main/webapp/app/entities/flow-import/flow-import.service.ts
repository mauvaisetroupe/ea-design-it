import axios from 'axios';

import { IFlowImport } from '@/shared/model/flow-import.model';

const baseApiUrl = 'api/flow-imports';
const apiForImportUrl = 'api/import';

export default class FlowImportService {
  public find(id: number): Promise<IFlowImport> {
    return new Promise<IFlowImport>((resolve, reject) => {
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

  public create(entity: IFlowImport): Promise<IFlowImport> {
    return new Promise<IFlowImport>((resolve, reject) => {
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

  public update(entity: IFlowImport): Promise<IFlowImport> {
    return new Promise<IFlowImport>((resolve, reject) => {
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

  public partialUpdate(entity: IFlowImport): Promise<IFlowImport> {
    return new Promise<IFlowImport>((resolve, reject) => {
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
  public uploadFile(files: File[]): Promise<any> {
    const formData: FormData = new FormData();

    files.forEach(file => formData.append('files', file));
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/flow/upload-files`, formData)
        .then(res => {
          console.log(res);
          resolve(res);
        })
        .catch(err => {
          console.log(err);
          reject(err);
        });
    });
  }
}
