import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

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
      maxLength: maxLength(1500),
    },
    comment: {
      maxLength: maxLength(1000),
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
  },
};

@Component({
  validations,
})
export default class FunctionalFlowUpdate extends Vue {
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlow: IFunctionalFlow = new FunctionalFlow();

  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;

  public functionalFlowSteps: IFunctionalFlowStep[] = [];

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];

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
  }

  public assignLandscape(): ILandscapeView {
    // is landcaspe ID pass as param
    let landscapeToSave: ILandscapeView;
    if (this.$route.query.landscapeViewId) {
      this.landscapeViews.forEach(landscape => {
        console.log(landscape.id + ' ...[' + this.$route.query.landscapeViewId + ']...');
        console.log('----[' + parseInt(this.$route.query.landscapeViewId as string) + ']---');
        if (landscape.id === parseInt(this.$route.query.landscapeViewId as string)) {
          landscapeToSave = landscape;
        }
      });
    }
    return landscapeToSave;
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
          let createdFunctionalFlow: IFunctionalFlow = param;
          // add to landscape if exist then save landscape
          let landscapeToSave = this.assignLandscape();
          console.log(landscapeToSave);
          if (landscapeToSave != null) {
            landscapeToSave.flows.push(createdFunctionalFlow);
            console.log('About to save landcsape : ');
            console.log(landscapeToSave);
            this.landscapeViewService()
              .update(landscapeToSave)
              .then(param2 => {
                this.isSaving = false;
                this.$router.go(-1);
                const message = 'A FunctionalFlow is created with identifier ' + param.id + ' for landscape ' + param2.id;
                this.$root.$bvToast.toast(message.toString(), {
                  toaster: 'b-toaster-top-center',
                  title: 'Success',
                  variant: 'success',
                  solid: true,
                  autoHideDelay: 5000,
                });
              });
          } else {
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
          }
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
    this.functionalFlowStepService()
      .retrieve()
      .then(res => {
        this.functionalFlowSteps = res.data;
      });
    this.ownerService()
      .retrieve()
      .then(res => {
        this.owners = res.data;
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
}
