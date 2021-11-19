import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

import { IEventData, EventData } from '@/shared/model/event-data.model';
import EventDataService from './event-data.service';

const validations: any = {
  eventData: {
    name: {},
    contractURL: {},
    documentationURL: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
  },
};

@Component({
  validations,
})
export default class EventDataUpdate extends Vue {
  @Inject('eventDataService') private eventDataService: () => EventDataService;
  @Inject('alertService') private alertService: () => AlertService;

  public eventData: IEventData = new EventData();

  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;

  public dataFlows: IDataFlow[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.eventDataId) {
        vm.retrieveEventData(to.params.eventDataId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.eventData.id) {
      this.eventDataService()
        .update(this.eventData)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A EventData is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.eventDataService()
        .create(this.eventData)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A EventData is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveEventData(eventDataId): void {
    this.eventDataService()
      .find(eventDataId)
      .then(res => {
        this.eventData = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.dataFlowService()
      .retrieve()
      .then(res => {
        this.dataFlows = res.data;
      });
  }
}
