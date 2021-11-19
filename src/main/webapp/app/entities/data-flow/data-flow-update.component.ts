import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import EventDataService from '@/entities/event-data/event-data.service';
import { IEventData } from '@/shared/model/event-data.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import { IDataFlow, DataFlow } from '@/shared/model/data-flow.model';
import DataFlowService from './data-flow.service';
import { Frequency } from '@/shared/model/enumerations/frequency.model';
import { Format } from '@/shared/model/enumerations/format.model';
import { FlowType } from '@/shared/model/enumerations/flow-type.model';

const validations: any = {
  dataFlow: {
    frequency: {},
    format: {},
    type: {},
    description: {
      maxLength: maxLength(1000),
    },
    resourceName: {},
    contractURL: {},
    documentationURL: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
    functionalFlows: {
      required,
    },
    flowInterface: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class DataFlowUpdate extends Vue {
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlow: IDataFlow = new DataFlow();

  @Inject('eventDataService') private eventDataService: () => EventDataService;

  public eventData: IEventData[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;

  public flowInterfaces: IFlowInterface[] = [];
  public frequencyValues: string[] = Object.keys(Frequency);
  public formatValues: string[] = Object.keys(Format);
  public flowTypeValues: string[] = Object.keys(FlowType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowId) {
        vm.retrieveDataFlow(to.params.dataFlowId);
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
    this.dataFlow.functionalFlows = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.dataFlow.id) {
      this.dataFlowService()
        .update(this.dataFlow)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlow is updated with identifier ' + param.id;
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
      this.dataFlowService()
        .create(this.dataFlow)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlow is created with identifier ' + param.id;
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

  public retrieveDataFlow(dataFlowId): void {
    this.dataFlowService()
      .find(dataFlowId)
      .then(res => {
        this.dataFlow = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.eventDataService()
      .retrieve()
      .then(res => {
        this.eventData = res.data;
      });
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
    this.flowInterfaceService()
      .retrieve()
      .then(res => {
        this.flowInterfaces = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
