import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowImport } from '@/shared/model/flow-import.model';

import FlowImportService from './flow-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FlowImport extends Vue {
  @Inject('flowImportService') private flowImportService: () => FlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public flowImports: IFlowImport[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFlowImports();
  }

  public clear(): void {
    this.retrieveAllFlowImports();
  }

  public retrieveAllFlowImports(): void {
    this.isFetching = true;
    this.flowImportService()
      .retrieve()
      .then(
        res => {
          this.flowImports = res.data;
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

  public prepareRemove(instance: IFlowImport): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFlowImport(): void {
    this.flowImportService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A FlowImport is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFlowImports();
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
