import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFlow } from '@/shared/model/data-flow.model';

import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class DataFlow extends Vue {
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public dataFlows: IDataFlow[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDataFlows();
  }

  public clear(): void {
    this.retrieveAllDataFlows();
  }

  public retrieveAllDataFlows(): void {
    this.isFetching = true;
    this.dataFlowService()
      .retrieve()
      .then(
        res => {
          this.dataFlows = res.data;
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

  public prepareRemove(instance: IDataFlow): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDataFlow(): void {
    this.dataFlowService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A DataFlow is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDataFlows();
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
