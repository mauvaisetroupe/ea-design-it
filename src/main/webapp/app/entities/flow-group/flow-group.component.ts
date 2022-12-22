import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFlowGroup } from '@/shared/model/flow-group.model';

import FlowGroupService from './flow-group.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FlowGroup extends Vue {
  @Inject('flowGroupService') private flowGroupService: () => FlowGroupService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public flowGroups: IFlowGroup[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFlowGroups();
  }

  public clear(): void {
    this.retrieveAllFlowGroups();
  }

  public retrieveAllFlowGroups(): void {
    this.isFetching = true;
    this.flowGroupService()
      .retrieve()
      .then(
        res => {
          this.flowGroups = res.data;
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

  public prepareRemove(instance: IFlowGroup): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFlowGroup(): void {
    this.flowGroupService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A FlowGroup is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFlowGroups();
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
