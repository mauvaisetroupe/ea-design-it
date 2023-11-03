import axios from 'axios';

const baseApiUrl = 'api/import';

export default class DataObjectService {
  public uploadFile(file: File): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/data-objects/upload-file`, formData)
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
