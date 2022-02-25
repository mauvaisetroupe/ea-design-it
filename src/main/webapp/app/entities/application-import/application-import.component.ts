import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IApplicationImport } from '@/shared/model/application-import.model';

import ApplicationImportService from './application-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ApplicationImport extends Vue {
  @Inject('applicationImportService') private applicationImportService: () => ApplicationImportService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public applicationImports: IApplicationImport[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllApplicationImports();
  }

  public clear(): void {
    this.retrieveAllApplicationImports();
  }

  public retrieveAllApplicationImports(): void {
    this.isFetching = true;
    this.applicationImportService()
      .retrieve()
      .then(
        res => {
          this.applicationImports = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IApplicationImport): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeApplicationImport(): void {
    this.applicationImportService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A ApplicationImport is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllApplicationImports();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
