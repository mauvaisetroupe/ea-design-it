import axios from 'axios';
import { type ICapabilityImportAnalysisDTO } from '@/shared/model/capability-import-analysis.model';
import type { ISummary } from '@/shared/model/summary-sheet.model';

const apiForImportUrl = 'api/import';

export default class CapabilityImportService {
  public uploadFileToAnalysis(file: File): Promise<ICapabilityImportAnalysisDTO> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/capability/upload-file`, formData)
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

  public confirmUploadedFile(dto: ICapabilityImportAnalysisDTO): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/capability/confirm-uploaded-file`, dto)
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

  public getSummary(file: File): Promise<ISummary[]> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return new Promise<any>((resolve, reject) => {
      axios
        .post(`${apiForImportUrl}/summary`, formData)
        .then(res => {
          console.log(res);
          resolve(res.data);
        })
        .catch(err => {
          console.log(err);
          reject(err);
        });
    });
  }
}
