import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFlowItem } from '@/shared/model/data-flow-item.model';

import DataFlowItemService from './data-flow-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class DataFlowItem extends Vue {
  @Inject('dataFlowItemService') private dataFlowItemService: () => DataFlowItemService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public dataFlowItems: IDataFlowItem[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDataFlowItems();
  }

  public clear(): void {
    this.retrieveAllDataFlowItems();
  }

  public retrieveAllDataFlowItems(): void {
    this.isFetching = true;
    this.dataFlowItemService()
      .retrieve()
      .then(
        res => {
          this.dataFlowItems = res.data;
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

  public prepareRemove(instance: IDataFlowItem): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDataFlowItem(): void {
    this.dataFlowItemService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A DataFlowItem is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDataFlowItems();
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
