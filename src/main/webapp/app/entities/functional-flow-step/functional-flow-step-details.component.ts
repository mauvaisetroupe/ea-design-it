import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import FunctionalFlowStepService from './functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FunctionalFlowStepDetails extends Vue {
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlowStep: IFunctionalFlowStep = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowStepId) {
        vm.retrieveFunctionalFlowStep(to.params.functionalFlowStepId);
      }
    });
  }

  public retrieveFunctionalFlowStep(functionalFlowStepId) {
    this.functionalFlowStepService()
      .find(functionalFlowStepId)
      .then(res => {
        this.functionalFlowStep = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
