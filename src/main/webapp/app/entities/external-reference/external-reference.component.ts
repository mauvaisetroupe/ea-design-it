import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IExternalReference } from '@/shared/model/external-reference.model';

import ExternalReferenceService from './external-reference.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ExternalReference extends Vue {
  @Inject('externalReferenceService') private externalReferenceService: () => ExternalReferenceService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public externalReferences: IExternalReference[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllExternalReferences();
  }

  public clear(): void {
    this.retrieveAllExternalReferences();
  }

  public retrieveAllExternalReferences(): void {
    this.isFetching = true;
    this.externalReferenceService()
      .retrieve()
      .then(
        res => {
          this.externalReferences = res.data;
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

  public prepareRemove(instance: IExternalReference): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeExternalReference(): void {
    this.externalReferenceService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A ExternalReference is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllExternalReferences();
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
