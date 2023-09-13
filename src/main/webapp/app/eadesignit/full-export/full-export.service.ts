import axios from 'axios';

const apiForImportUrl = 'api/import';
const apiForExportUrl = 'api/export';

export default class FullExportService {
  public downloadFile(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${apiForExportUrl}/all`, {
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
}
