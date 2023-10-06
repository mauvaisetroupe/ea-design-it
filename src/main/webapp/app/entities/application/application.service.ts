import axios from 'axios';

import { IApplication } from '@/shared/model/application.model';
import { ICapability } from '@/shared/model/capability.model';
import { PlantumlDTO } from '@/shared/model/plantuml-dto';

const baseApiUrl = 'api/applications';
const basePlantUMLApiUrl = 'api/plantuml/application/get-svg';
const baseCapabilitiesPlantUMLApiUrl = 'api/plantuml/application/capability/get-svg';
const applicationStructurePlantUMLApiUrl = 'api/plantuml/application/structure/get-svg';

export default class ApplicationService {
  public find(id: number): Promise<IApplication> {
    return new Promise<IApplication>((resolve, reject) => {
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
      if (sessionStorage.getItem('applications')) {
        try {
          const parsedJSON = JSON.parse(sessionStorage.getItem('applications'));
          console.log('retrieving applications from localStorage');
          resolve(parsedJSON);
        } catch (e) {
          sessionStorage.removeItem('applications');
          reject(e);
        }
      } else {
        axios
          .get(baseApiUrl)
          .then(res => {
            const json = JSON.stringify(res);
            console.log('storing applications in localStorage');
            sessionStorage.setItem('applications', json);
            resolve(res);
          })
          .catch(err => {
            reject(err);
          });
      }
    });
  }

  public delete(id: number): Promise<any> {
    sessionStorage.removeItem('applications');
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

  public create(entity: IApplication): Promise<IApplication> {
    sessionStorage.removeItem('applications');
    return new Promise<IApplication>((resolve, reject) => {
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

  public update(entity: IApplication): Promise<IApplication> {
    sessionStorage.removeItem('applications');
    return new Promise<IApplication>((resolve, reject) => {
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

  public partialUpdate(entity: IApplication): Promise<IApplication> {
    sessionStorage.removeItem('applications');
    return new Promise<IApplication>((resolve, reject) => {
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

  public getPlantUML(
    id: number,
    layout: string,
    groupComponents: boolean,
    showLabels: boolean,
    showLabelIfNumberapplicationsLessThan: number
  ) {
    const params = {
      layout: layout,
      groupComponents: groupComponents,
      showLabels: showLabels,
      showLabelIfNumberapplicationsLessThan: showLabelIfNumberapplicationsLessThan,
    };
    return new Promise<PlantumlDTO>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl}/${id}`, { params })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getCapabilitiesPlantUML(id: number) {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseCapabilitiesPlantUMLApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getApplicationStructurePlantUML(id: number) {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${applicationStructurePlantUMLApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getCapabilities(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}/capabilities`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
