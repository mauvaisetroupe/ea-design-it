import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IEventData } from '@/shared/model/event-data.model';

import EventDataService from './event-data.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class EventData extends Vue {
  @Inject('eventDataService') private eventDataService: () => EventDataService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public eventData: IEventData[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllEventDatas();
  }

  public clear(): void {
    this.retrieveAllEventDatas();
  }

  public retrieveAllEventDatas(): void {
    this.isFetching = true;
    this.eventDataService()
      .retrieve()
      .then(
        res => {
          this.eventData = res.data;
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

  public prepareRemove(instance: IEventData): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeEventData(): void {
    this.eventDataService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A EventData is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllEventDatas();
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
