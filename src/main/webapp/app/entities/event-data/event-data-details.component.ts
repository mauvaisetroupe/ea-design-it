import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEventData } from '@/shared/model/event-data.model';
import EventDataService from './event-data.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EventDataDetails extends Vue {
  @Inject('eventDataService') private eventDataService: () => EventDataService;
  @Inject('alertService') private alertService: () => AlertService;

  public eventData: IEventData = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventDataId) {
        vm.retrieveEventData(to.params.eventDataId);
      }
    });
  }

  public retrieveEventData(eventDataId) {
    this.eventDataService()
      .find(eventDataId)
      .then(res => {
        this.eventData = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
