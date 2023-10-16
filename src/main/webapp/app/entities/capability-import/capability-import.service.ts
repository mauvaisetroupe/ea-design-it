import { ICapabilityImportAnalysisDTO } from '@/shared/model/capability-import-analysis.model';
import axios from 'axios';

const apiForImportUrl = 'api/import';

export default class CapabilityImportService {
  public uploadFileToAnalysis(file: File): Promise<ICapabilityImportAnalysisDTO> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/capability/upload-file/analyze`, formData)
        .then(res => {
          console.log(res.data);
          resolve(res.data);
        })
        .catch(err => {
          console.log(err);
          reject(err);
        });
    });
  }

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

  public uploadMappingFile(file: File, sheetnames: string[]): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    sheetnames.forEach(sheetname => {
      formData.append('sheetnames', sheetname);
    });

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

  public getSummary(file: File): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/summary`, formData)
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
