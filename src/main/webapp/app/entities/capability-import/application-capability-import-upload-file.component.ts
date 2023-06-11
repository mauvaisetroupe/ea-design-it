import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import CapabilityImportService from './capability-import.service';
import AlertService from '@/shared/alert/alert.service';
import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from '../landscape-view/landscape-view.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class CapabilityImport extends Vue {
  @Inject('capabilityImportService') private capabilityImportService: () => CapabilityImportService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;

  public existingLandscapes: ILandscapeView[] = null;
  public potentialLandscape: String[] = [];
  public selectedLandscape: String[] = [];

  public mounted(): void {
    this.retrieveAllLandscapeViews();
  }

  public retrieveAllLandscapeViews(): void {
    this.isFetching = true;
    this.landscapeViewService()
      .retrieve()
      .then(
        res => {
          this.existingLandscapes = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public capabilitiesImports = [];
  public excelFile: File = null;
  public isFetching = false;
  public fileSubmited = false;
  public rowsLoaded = false;
  public excelFileName = 'Browse File';
  public sheetnames = [];
  public checkedNames = [];

  public handleFileUpload(event): void {
    this.excelFile = event.target.files[0];
    this.excelFileName = this.excelFile.name;
  }

  public submitFile(): void {
    this.isFetching = true;
    this.fileSubmited = true;
    this.capabilityImportService()
      .uploadMappingFile(this.excelFile, this.checkedNames)
      .then(
        res => {
          this.capabilitiesImports = res.data;
          this.isFetching = false;
          this.rowsLoaded = true;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public getSheetnames(): void {
    this.isFetching = true;
    this.sheetnames = [];
    this.capabilityImportService()
      .getSheetNames(this.excelFile)
      .then(
        res => {
          this.isFetching = false;
          this.sheetnames = res.data;
          this.sheetnames.forEach(name => {
            if (name.startsWith('ADD')) {
              this.checkedNames.push(name);
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
}
