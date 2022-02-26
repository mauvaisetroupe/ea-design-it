import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

import FunctionalFlowStepService from './functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FunctionalFlowStep extends Vue {
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public functionalFlowSteps: IFunctionalFlowStep[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFunctionalFlowSteps();
  }

  public clear(): void {
    this.retrieveAllFunctionalFlowSteps();
  }

  public retrieveAllFunctionalFlowSteps(): void {
    this.isFetching = true;
    this.functionalFlowStepService()
      .retrieve()
      .then(
        res => {
          this.functionalFlowSteps = res.data;
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

  public prepareRemove(instance: IFunctionalFlowStep): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFunctionalFlowStep(): void {
    this.functionalFlowStepService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A FunctionalFlowStep is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFunctionalFlowSteps();
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
