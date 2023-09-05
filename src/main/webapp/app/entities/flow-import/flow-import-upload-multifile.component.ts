import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowImport } from '@/shared/model/flow-import.model';

import FlowImportService from './flow-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FlowImportUploadMultiFile extends Vue {
  @Inject('flowImportService') private flowImportService: () => FlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  //public flowImports: IFlowImport[] = [];
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileName = 'Browse File';

  public sheetnames: string[] = [];
  public checkedNames: string[] = [];

  public dtos = null;
  public notFilteredDtos = null;

  public handleFileUpload(event): void {
    console.log(event);
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  // STEP 1 - Upload file and retreive all sheet with name starting with FLW
  public getSheetnames(): void {
    this.isFetching = true;
    this.flowImportService()
      .getSheetNames(this.excelFile)
      .then(
        res => {
          this.isFetching = false;
          var tmpsheetnames = res.data;
          tmpsheetnames.forEach((name, i) => {
            if (name.startsWith('FLW')) {
              this.checkedNames.push(name);
              this.sheetnames.push(name);
            }
          });
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public selectAll() {
    this.checkedNames = this.sheetnames;
  }

  public selectNone() {
    this.checkedNames = [];
  }

  // Step 2 - Submit de file and selected sheet names

  public submitFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.flowImportService()
      .uploadMultipleFile(this.excelFile, this.checkedNames)
      .then(
        res => {
          this.dtos = res.data;
          this.isFetching = false;
          this.rowsLoaded = true;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  // step 3 - Give possibility to filer errors

  public filterErrors() {
    this.dtos = [];
    this.notFilteredDtos.forEach(dto => {
      let newdto = {};
      const newimport = [];
      dto.flowImports.forEach(elem => {
        const flowImport = elem as IFlowImport;
        if (
          flowImport.importFunctionalFlowStatus === 'ERROR' ||
          flowImport.importInterfaceStatus === 'ERROR' ||
          flowImport.importDataFlowStatus === 'ERROR'
        ) {
          newimport.push(flowImport);
        }
      });
      newdto = {
        excelFileName: dto.excelFileName,
        flowImports: newimport,
      };
      this.dtos.push(newdto);
    });
  }

  public getErrors() {
    const errors = [];
    this.notFilteredDtos.forEach(dto => {
      let line = 1;
      dto.flowImports.forEach(elem => {
        const flowImport = elem;
        if (
          flowImport.importFunctionalFlowStatus === 'ERROR' ||
          flowImport.importInterfaceStatus === 'ERROR' ||
          flowImport.importDataFlowStatus === 'ERROR'
        ) {
          flowImport.id = dto.excelFileName + '_' + line.toString();
          const errorRow = {
            ...flowImport,
          };
          errors.push(errorRow);
          console.log(errorRow);
        }
        line = line + 1;
      });
    });
    return errors;
  }

  public exportErrors() {
    const errors = this.getErrors();
    let csvContent = 'data:text/csv;charset=utf-8,';
    csvContent += [Object.keys(errors[0]).join(';'), ...errors.map(row => Object.values(row).join(';').replace(/\n/gm, ''))]
      .join('\n')
      .replace(/(^\[)|(\]$)/gm, '');
    const data = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', data);
    link.setAttribute('download', 'export.csv');
    link.click();
  }
}
