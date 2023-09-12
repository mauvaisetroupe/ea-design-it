import { Component, Vue, Inject } from 'vue-property-decorator';
import ExternalSystemService from '@/entities/external-system/external-system.service';
import AlertService from '@/shared/alert/alert.service';
import { IExternalSystem } from '@/shared/model/external-system.model';

@Component
export default class ExternalSystemUploadFile extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('externalSystemService') private externalSystemService: () => ExternalSystemService;

  //public flowImports: IFlowImport[] = [];
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileName = 'Browse File';
  public externalSystems: IExternalSystem[] = [];

  public handleFileUpload(event): void {
    console.log(event);
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  public submitFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.externalSystemService()
      .uploadFile(this.excelFile)
      .then(
        res => {
          this.externalSystems = res.data;
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
