import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IProtocol } from '@/shared/model/protocol.model';

import ProtocolService from './protocol.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Protocol extends Vue {
  @Inject('protocolService') private protocolService: () => ProtocolService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public protocols: IProtocol[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllProtocols();
  }

  public clear(): void {
    this.retrieveAllProtocols();
  }

  public retrieveAllProtocols(): void {
    this.isFetching = true;
    this.protocolService()
      .retrieve()
      .then(
        res => {
          this.protocols = res.data;
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

  public prepareRemove(instance: IProtocol): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeProtocol(): void {
    this.protocolService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Protocol is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllProtocols();
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
