import axios from 'axios';

import { type IFlowImport } from '@/shared/model/flow-import.model';
import type { ISummary } from 'shared/model/summary-sheet.model';

const baseApiUrl = 'api/flow-imports';
const apiForImportUrl = 'api/import';
const apiForExportUrl = 'api/export';

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
  public uploadMultipleFile(file: File, sheetnames: string[]): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    sheetnames.forEach(sheetname => {
      formData.append('sheetnames', sheetname);
    });

    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/flow/upload-multi-file`, formData)
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

  public downloadFile(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${apiForExportUrl}/landscape/${id}`, {
          responseType: 'blob',
        })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getSheetNames(file: File): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/sheetnames`, formData)
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

  public getSummary(file: File): Promise<ISummary[]> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/summary`, formData)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
