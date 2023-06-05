import axios from 'axios';

const apiForExportUrl = 'api/export';

export default class ExportService {
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
