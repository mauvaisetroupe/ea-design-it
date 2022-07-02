import axios from 'axios';

const apiForImportUrl = 'api/import';

export default class CapabilityImportService {
  public uploadFile(file: File): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/capability/upload-file`, formData)
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

  public uploadMappingFile(file: File, sheetname: string[]): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    for (var i = 0; i < sheetname.length; i++) {
      formData.append('sheetname', sheetname[i]);
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/application/capability/upload-file`, formData)
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
}
