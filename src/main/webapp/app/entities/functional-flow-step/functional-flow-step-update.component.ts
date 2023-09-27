import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

import FlowGroupService from '@/entities/flow-group/flow-group.service';
import { IFlowGroup } from '@/shared/model/flow-group.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import { IFunctionalFlowStep, FunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import FunctionalFlowStepService from './functional-flow-step.service';

const validations: any = {
  functionalFlowStep: {
    description: {
      maxLength: maxLength(500),
    },
    stepOrder: {},
    flowInterface: {
      required,
    },
    flow: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FunctionalFlowStepUpdate extends Vue {
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlowStep: IFunctionalFlowStep = new FunctionalFlowStep();

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;

  public flowInterfaces: IFlowInterface[] = [];

  @Inject('flowGroupService') private flowGroupService: () => FlowGroupService;

  public flowGroups: IFlowGroup[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowStepId) {
        vm.retrieveFunctionalFlowStep(to.params.functionalFlowStepId);
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
    if (this.functionalFlowStep.id) {
      this.functionalFlowStepService()
        .update(this.functionalFlowStep)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FunctionalFlowStep is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
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
      this.functionalFlowStepService()
        .create(this.functionalFlowStep)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FunctionalFlowStep is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
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

  public retrieveFunctionalFlowStep(functionalFlowStepId): void {
    this.functionalFlowStepService()
      .find(functionalFlowStepId)
      .then(res => {
        this.functionalFlowStep = res;
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
    this.flowGroupService()
      .retrieve()
      .then(res => {
        this.flowGroups = res.data;
      });
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
  }
}
