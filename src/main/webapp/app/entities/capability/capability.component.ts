import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICapability } from '@/shared/model/capability.model';

import CapabilityService from './capability.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Capability extends Vue {
  @Inject('capabilityService') private capabilityService: () => CapabilityService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public capabilities: ICapability[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCapabilitys();
  }

  public clear(): void {
    this.retrieveAllCapabilitys();
  }

  public retrieveAllCapabilitys(): void {
    this.isFetching = true;
    this.capabilityService()
      .retrieve()
      .then(
        res => {
          this.capabilities = res.data;
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

  public prepareRemove(instance: ICapability): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCapability(): void {
    this.capabilityService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Capability is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCapabilitys();
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
