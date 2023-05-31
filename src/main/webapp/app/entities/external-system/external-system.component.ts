import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IExternalSystem } from '@/shared/model/external-system.model';

import ExternalSystemService from './external-system.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ExternalSystem extends Vue {
  @Inject('externalSystemService') private externalSystemService: () => ExternalSystemService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public externalSystems: IExternalSystem[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllExternalSystems();
  }

  public clear(): void {
    this.retrieveAllExternalSystems();
  }

  public retrieveAllExternalSystems(): void {
    this.isFetching = true;
    this.externalSystemService()
      .retrieve()
      .then(
        res => {
          this.externalSystems = res.data;
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

  public prepareRemove(instance: IExternalSystem): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeExternalSystem(): void {
    this.externalSystemService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A ExternalSystem is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllExternalSystems();
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
