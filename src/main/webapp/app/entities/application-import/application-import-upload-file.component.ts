import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IApplicationImport } from '@/shared/model/application-import.model';
import ApplicationImportService from './application-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ApplicationImportUploadFile extends Vue {
  @Inject('applicationImportService') private applicationImportService: () => ApplicationImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public applicationImports: IApplicationImport[] = [];
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
    this.applicationImportService()
      .uploadFile(this.excelFile)
      .then(
        res => {
          this.applicationImports = res.data;
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
