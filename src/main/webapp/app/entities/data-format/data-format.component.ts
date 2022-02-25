import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDataFormat } from '@/shared/model/data-format.model';

import DataFormatService from './data-format.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class DataFormat extends Vue {
  @Inject('dataFormatService') private dataFormatService: () => DataFormatService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public dataFormats: IDataFormat[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDataFormats();
  }

  public clear(): void {
    this.retrieveAllDataFormats();
  }

  public retrieveAllDataFormats(): void {
    this.isFetching = true;
    this.dataFormatService()
      .retrieve()
      .then(
        res => {
          this.dataFormats = res.data;
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

  public prepareRemove(instance: IDataFormat): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDataFormat(): void {
    this.dataFormatService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A DataFormat is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDataFormats();
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
