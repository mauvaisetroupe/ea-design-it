import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

import { IFlowGroup, FlowGroup } from '@/shared/model/flow-group.model';
import FlowGroupService from './flow-group.service';

const validations: any = {
  flowGroup: {
    order: {
      required,
      numeric,
    },
    title: {
      maxLength: maxLength(100),
    },
    url: {
      maxLength: maxLength(500),
    },
    flow: {
      required,
    },
    steps: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FlowGroupUpdate extends Vue {
  @Inject('flowGroupService') private flowGroupService: () => FlowGroupService;
  @Inject('alertService') private alertService: () => AlertService;

  public flowGroup: IFlowGroup = new FlowGroup();

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];

  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;

  public functionalFlowSteps: IFunctionalFlowStep[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowGroupId) {
        vm.retrieveFlowGroup(to.params.flowGroupId);
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
    if (this.flowGroup.id) {
      this.flowGroupService()
        .update(this.flowGroup)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FlowGroup is updated with identifier ' + param.id;
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
      this.flowGroupService()
        .create(this.flowGroup)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FlowGroup is created with identifier ' + param.id;
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

  public retrieveFlowGroup(flowGroupId): void {
    this.flowGroupService()
      .find(flowGroupId)
      .then(res => {
        this.flowGroup = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
    this.functionalFlowStepService()
      .retrieve()
      .then(res => {
        this.functionalFlowSteps = res.data;
      });
  }
}
