import { ILandscapeView } from '@/shared/model/landscape-view.model';
import axios from 'axios';

const apiForImportUrl = 'api/import';
const apiForExportUrl = 'api/export';

export default class FullExportService {
  public downloadFile(
    applications: boolean,
    applicationComponents: boolean,
    owner: boolean,
    externalSystem: boolean,
    capabilities: boolean,
    landscapes: number[],
    capabilitiesMapping: number[],
    capabilitiesMappingWithNoLandscape: boolean,
    functionalFlowsWhithNoLandscape: boolean
  ): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${apiForExportUrl}/all`, {
          responseType: 'blob',
          params: {
            applications: applications,
            applicationComponents: applicationComponents,
            owner: owner,
            externalSystem: externalSystem,
            capabilities: capabilities,
            landscapes: landscapes,
            capabilitiesMapping: capabilitiesMapping,
            capabilitiesMappingWithNoLandscape: capabilitiesMappingWithNoLandscape,
            functionalFlowsWhithNoLandscape: functionalFlowsWhithNoLandscape,
          },
        })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
