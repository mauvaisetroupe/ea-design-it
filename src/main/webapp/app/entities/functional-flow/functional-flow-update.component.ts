import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import { ILandscapeView } from '@/shared/model/landscape-view.model';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

import { IFunctionalFlow, FunctionalFlow } from '@/shared/model/functional-flow.model';
import FunctionalFlowService from './functional-flow.service';

const validations: any = {
  functionalFlow: {
    alias: {},
    description: {
      maxLength: maxLength(1000),
    },
    comment: {
      maxLength: maxLength(500),
    },
    status: {},
    documentationURL: {
      maxLength: maxLength(500),
    },
    documentationURL2: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
    landscapes: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FunctionalFlowUpdate extends Vue {
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlow: IFunctionalFlow = new FunctionalFlow();

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;

  public flowInterfaces: IFlowInterface[] = [];

  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;

  public landscapeViews: ILandscapeView[] = [];

  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;

  public dataFlows: IDataFlow[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowId) {
        vm.retrieveFunctionalFlow(to.params.functionalFlowId);
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
    this.functionalFlow.interfaces = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.functionalFlow.id) {
      this.functionalFlowService()
        .update(this.functionalFlow)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FunctionalFlow is updated with identifier ' + param.id;
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
      this.functionalFlowService()
        .create(this.functionalFlow)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FunctionalFlow is created with identifier ' + param.id;
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

  public retrieveFunctionalFlow(functionalFlowId): void {
    this.functionalFlowService()
      .find(functionalFlowId)
      .then(res => {
        this.functionalFlow = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.flowInterfaceService()
      .retrieve()
      .then(res => {
        this.flowInterfaces = res.data;
      });
    this.landscapeViewService()
      .retrieve()
      .then(res => {
        this.landscapeViews = res.data;
      });
    this.dataFlowService()
      .retrieve()
      .then(res => {
        this.dataFlows = res.data;
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
