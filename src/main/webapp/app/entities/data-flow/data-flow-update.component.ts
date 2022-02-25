import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DataFlowItemService from '@/entities/data-flow-item/data-flow-item.service';
import { IDataFlowItem } from '@/shared/model/data-flow-item.model';

import DataFormatService from '@/entities/data-format/data-format.service';
import { IDataFormat } from '@/shared/model/data-format.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import { IDataFlow, DataFlow } from '@/shared/model/data-flow.model';
import DataFlowService from './data-flow.service';
import { Frequency } from '@/shared/model/enumerations/frequency.model';

const validations: any = {
  dataFlow: {
    resourceName: {
      required,
    },
    resourceType: {},
    description: {
      maxLength: maxLength(1000),
    },
    frequency: {},
    contractURL: {
      maxLength: maxLength(500),
    },
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
export default class DataFlowUpdate extends Vue {
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlow: IDataFlow = new DataFlow();

  @Inject('dataFlowItemService') private dataFlowItemService: () => DataFlowItemService;

  public dataFlowItems: IDataFlowItem[] = [];

  @Inject('dataFormatService') private dataFormatService: () => DataFormatService;

  public dataFormats: IDataFormat[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;

  public flowInterfaces: IFlowInterface[] = [];
  public frequencyValues: string[] = Object.keys(Frequency);
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
    this.dataFlowItemService()
      .retrieve()
      .then(res => {
        this.dataFlowItems = res.data;
      });
    this.dataFormatService()
      .retrieve()
      .then(res => {
        this.dataFormats = res.data;
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
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
