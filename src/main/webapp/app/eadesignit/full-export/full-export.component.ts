import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';

import AlertService from '@/shared/alert/alert.service';
import FullExportService from './full-export.service';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import { ILandscapeView } from '@/shared/model/landscape-view.model';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FullExport extends Vue {
  @Inject('fullExportService') private fullExportService: () => FullExportService;
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public isFetching = false;

  public applications = true;
  public applicationComponents = true;
  public landscapes: ILandscapeView[] = [];
  public checkedLandscapes: ILandscapeView[] = [];
  public owner = true;
  public externalSystem = true;
  public capabilities = true;
  public capabilitiesMapping: ILandscapeView[] = [];
  public checkedCapabilitiesMapping: ILandscapeView[] = [];
  public capabilitiesMappingWithNoLandscape = true;

  public submited = false;

  public mounted(): void {
    this.retrieveAllLandscapeViews();
  }

  public retrieveAllLandscapeViews(): void {
    this.isFetching = true;
    this.landscapeViewService()
      .retrieve()
      .then(
        res => {
          this.landscapes = res.data;
          this.checkedLandscapes = res.data;
          this.capabilitiesMapping = res.data;
          this.checkedCapabilitiesMapping = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public selectAllLansdcape() {
    if (!this.submited) {
      this.checkedLandscapes = [];
      this.checkedLandscapes.push(...this.landscapes);
    }
  }

  public selectNoLandscape() {
    if (!this.submited) this.checkedLandscapes = [];
  }

  public selectAllMapping() {
    if (!this.submited) {
      this.checkedCapabilitiesMapping = [];
      this.checkedCapabilitiesMapping.push(...this.capabilitiesMapping);
    }
  }

  public selectNoMapping() {
    if (!this.submited) this.checkedCapabilitiesMapping = [];
  }

  public exportExcel() {
    this.submited = true;
    this.isFetching = true;
    this.fullExportService()
      .downloadFile()
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'application/vnd.ms-excel',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        const today = new Date().toISOString().split('T')[0];
        const time = new Date().toLocaleTimeString().replace(' ', '_');
        link.setAttribute('download', 'full-data-export-' + today + '-' + time + '.xlsx');
        document.body.appendChild(link);
        link.click();
        this.isFetching = false;
      });
  }
}
