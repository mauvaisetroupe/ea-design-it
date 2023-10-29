import axios from 'axios';

import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import { type PlantumlDTO } from '@/shared/model/plantuml-dto';
import { type ILandscapeDTO } from '@/shared/model/landscape-view.model';

const baseApiUrl = 'api/landscape-views';
const basePlantUMLApiUrl = 'api/plantuml/landscape-view';
const baseDrawIOApiUrl = 'api/drawio/landscape-view/get-xml';

export default class LandscapeViewService {
  public find(id: number): Promise<ILandscapeDTO> {
    return new Promise<ILandscapeDTO>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
          console.log(res.data);
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

  public delete(id: number, deleteFunctionalFlows: boolean, deleteFlowInterfaces: boolean, deleteDatas: boolean): Promise<any> {
    const params = { deleteFunctionalFlows: deleteFunctionalFlows, deleteFlowInterfaces: deleteFlowInterfaces, deleteDatas: deleteDatas };
    console.log(params);
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`, { params })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: ILandscapeView): Promise<ILandscapeView> {
    return new Promise<ILandscapeView>((resolve, reject) => {
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

  public update(entity: ILandscapeView): Promise<ILandscapeView> {
    return new Promise<ILandscapeView>((resolve, reject) => {
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

  public partialUpdate(entity: ILandscapeView): Promise<ILandscapeView> {
    return new Promise<ILandscapeView>((resolve, reject) => {
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
    showLabelIfNumberapplicationsLessThan: number,
  ) {
    console.log('showlabels : ' + showLabels);
    return new Promise<PlantumlDTO>((resolve, reject) => {
      const params = {
        layout: layout,
        groupComponents: groupComponents,
        showLabels: showLabels,
        showLabelIfNumberapplicationsLessThan: showLabelIfNumberapplicationsLessThan,
      };
      axios
        .get(`${basePlantUMLApiUrl}/get-svg/${id}`, { params })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getPlantUMLSource(id: number) {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${basePlantUMLApiUrl}/get-source/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public getDrawIO(id: number) {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseDrawIOApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public deleteDrawInformation(id: number): Promise<ILandscapeView> {
    return new Promise<ILandscapeView>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${id}/delete-draw`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
