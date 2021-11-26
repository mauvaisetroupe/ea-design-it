import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFlowImport } from '@/shared/model/data-flow-import.model';

import DataFlowImportService from './data-flow-import.service';
import AlertService from '@/shared/alert/alert.service';
@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ApplicationImportUploadFile extends Vue {
  @Inject('dataFlowImportService') private dataFlowImportService: () => DataFlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlowImports: IDataFlowImport[] = [];
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileName = 'Browse File';

  public handleFileUpload(event): void {
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  public submitFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.dataFlowImportService()
      .uploadFile(this.excelFile)
      .then(
        res => {
          this.dataFlowImports = res.data;
          this.isFetching = false;
          this.rowsLoaded = true;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }
}
