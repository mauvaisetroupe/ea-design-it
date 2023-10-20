import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import CapabilityImportService from './capability-import.service';
import AlertService from '@/shared/alert/alert.service';
import { IApplicationCapabilityImport, IApplicationCapabilityImportItem } from '@/shared/model/application-capability-import.model';
import { ISummary } from '@/shared/model/summary-sheet.model';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class CapabilityImport extends Vue {
  @Inject('capabilityImportService') private capabilityImportService: () => CapabilityImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public dtos: IApplicationCapabilityImport[] = [];
  public notFilteredDtos: IApplicationCapabilityImport[] = [];
  public excelFileName = 'Browse File';
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;

  public checkedNames: string[] = [];
  public summary: ISummary[] = [];

  // STEP 1 - Upload file and retreive all sheet with name starting with FLW

  public handleFileUpload(event): void {
    console.log(event);
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  public getSheetnames(): void {
    this.isFetching = true;
    this.capabilityImportService()
      .getSummary(this.excelFile)
      .then(
        res => {
          this.isFetching = false;
          this.summary = res.data.filter(sum => sum).filter(sum => sum.entityType === 'Capability Mapping');
          this.checkedNames = this.summary.map(sum => sum.sheetName);
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
      this.checkedNames.push(...this.summary.map(sum => sum.sheetName));
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
    this.capabilityImportService()
      .uploadMappingFile(this.excelFile, [sheetToProcess])
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
      const newitems: IApplicationCapabilityImportItem[] = [];
      dto.dtos.forEach(item => {
        if (item.importStatus === 'ERROR') {
          newitems.push(item);
        }
      });
      let newdto: IApplicationCapabilityImport = {}; 
      newdto.dtos = newitems;
      newdto.sheetname = dto.sheetname;
      this.dtos.push(newdto);
    });
  }
}
