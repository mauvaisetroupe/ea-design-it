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
  public landscapeMap = {};

  public dtos = [];
  public notFilteredDtos = [];

  public handleFileUpload(event): void {
    console.log(event);
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  // STEP 1 - Upload file and retreive all sheet with name starting with FLW
  public getSheetnames(): void {
    this.isFetching = true;
    this.flowImportService()
      .getSummary(this.excelFile)
      .then(
        res => {
          this.isFetching = false;
          const summary = res.data;
          summary.forEach(row => {
            if (row['entity.type'] === 'Landscape') {
              const sheetname: string = row['sheet hyperlink'];
              const landscape: string = row['landscape.name'];
              this.checkedNames.push(sheetname);
              this.sheetnames.push(sheetname);
              this.landscapeMap[sheetname] = landscape;
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
    if (!this.fileSubmited) {
      this.checkedNames = [];
      this.checkedNames.push(...this.sheetnames);
    }
  }

  public selectNone() {
    if (!this.fileSubmited) this.checkedNames = [];
  }

  // Step 2 - Submit de file and selected sheet names

  public submitFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    // send file n times, sheet by sheet
    // this is not optimal, but it's the easier way to have a reactive behavior and avoid time out
    // serialized to avoid database transactional problem
    this.uploadOneSheet();
  }

  public uploadOneSheet() {
    const sheetToProcess: string = this.checkedNames.shift();
    this.flowImportService()
      .uploadMultipleFile(this.excelFile, [sheetToProcess])
      .then(
        res => {
          this.dtos.push(...res.data);
          this.notFilteredDtos.push(...res.data);
          this.rowsLoaded = true;
          if (this.checkedNames.length > 0) {
            this.uploadOneSheet();
          } else {
            this.isFetching = false;
          }
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
}
