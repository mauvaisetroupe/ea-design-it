import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFlowImport } from '@/shared/model/data-flow-import.model';

import DataFlowImportService from './data-flow-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class DataFlowImport extends Vue {
  @Inject('dataFlowImportService') private dataFlowImportService: () => DataFlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public dataFlowImports: IDataFlowImport[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDataFlowImports();
  }

  public clear(): void {
    this.retrieveAllDataFlowImports();
  }

  public retrieveAllDataFlowImports(): void {
    this.isFetching = true;
    this.dataFlowImportService()
      .retrieve()
      .then(
        res => {
          this.dataFlowImports = res.data;
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

  public prepareRemove(instance: IDataFlowImport): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDataFlowImport(): void {
    this.dataFlowImportService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A DataFlowImport is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDataFlowImports();
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
