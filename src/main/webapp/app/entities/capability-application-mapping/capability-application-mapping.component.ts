import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';

import CapabilityApplicationMappingService from './capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class CapabilityApplicationMapping extends Vue {
  @Inject('capabilityApplicationMappingService') private capabilityApplicationMappingService: () => CapabilityApplicationMappingService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public capabilityApplicationMappings: ICapabilityApplicationMapping[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCapabilityApplicationMappings();
  }

  public clear(): void {
    this.retrieveAllCapabilityApplicationMappings();
  }

  public retrieveAllCapabilityApplicationMappings(): void {
    this.isFetching = true;
    this.capabilityApplicationMappingService()
      .retrieve()
      .then(
        res => {
          this.capabilityApplicationMappings = res.data;
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

  public prepareRemove(instance: ICapabilityApplicationMapping): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCapabilityApplicationMapping(): void {
    this.capabilityApplicationMappingService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A CapabilityApplicationMapping is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCapabilityApplicationMappings();
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
