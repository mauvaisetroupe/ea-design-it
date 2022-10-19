import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import ProtocolService from '@/entities/protocol/protocol.service';
import { IProtocol } from '@/shared/model/protocol.model';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import { FunctionalFlowStep, IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

import { IFlowInterface, FlowInterface } from '@/shared/model/flow-interface.model';
import FlowInterfaceService from './flow-interface.service';
import ApplicationComponent from '../application-component/application-component.component';

const validations: any = {
  flowInterface: {
    alias: {
      required,
    },
    status: {},
    documentationURL: {
      maxLength: maxLength(500),
    },
    documentationURL2: {
      maxLength: maxLength(500),
    },
    description: {
      maxLength: maxLength(1500),
    },
    startDate: {},
    endDate: {},
    source: {
      required,
    },
    target: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FlowInterfaceUpdate extends Vue {
  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  @Inject('alertService') private alertService: () => AlertService;

  public flowInterface: IFlowInterface = new FlowInterface();

  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;

  public dataFlows: IDataFlow[] = [];

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];

  @Inject('protocolService') private protocolService: () => ProtocolService;

  public protocols: IProtocol[] = [];

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowInterfaceId) {
        vm.retrieveFlowInterface(to.params.flowInterfaceId);
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

  public assignFunctionalFlow(): IFunctionalFlow {
    // is landcaspe ID pass as param
    let functionalFlowToSave: IFunctionalFlow;
    if (this.$route.query.functionalFlowId) {
      this.functionalFlows.forEach(functionalFlow => {
        console.log(functionalFlow.id + '---' + this.$route.query.functionalFlowId);
        if (functionalFlow.id === parseInt(this.$route.query.functionalFlowId as string)) {
          console.log('FunctionalFlow : ' + functionalFlow.id);
          functionalFlowToSave = functionalFlow;
        }
      });
    }
    return functionalFlowToSave;
  }

  public assignSourceAndTarget() {
    if (this.$route.query.sourceId || this.$route.query.targetId) {
      this.applications.forEach(a => {
        if (this.$route.query.sourceId && a.id === parseInt(this.$route.query.sourceId as string)) {
          this.flowInterface.source = a;
        }

        if (this.$route.query.targetId && a.id === parseInt(this.$route.query.targetId as string)) {
          this.flowInterface.target = a;
        }
      });
    }
  }

  public assignProtocol() {
    if (this.$route.query.protocolId) {
      this.protocols.forEach(p => {
        if (p.id === parseInt(this.$route.query.protocolId as string)) {
          this.flowInterface.protocol = p;
        }
      });
    }
  }

  public save(): void {
    this.isSaving = true;
    if (this.flowInterface.id) {
      this.flowInterfaceService()
        .update(this.flowInterface)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FlowInterface is updated with identifier ' + param.id;
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
      this.flowInterfaceService()
        .create(this.flowInterface)
        .then(param => {
          let createdInterface: IFunctionalFlow = param;
          let functionalFlowToSave = this.assignFunctionalFlow();
          if (functionalFlowToSave != null) {
            let step: IFunctionalFlowStep = new FunctionalFlowStep();
            step.flowInterface = createdInterface;
            step.flow = functionalFlowToSave;
            this.functionalFlowStepService()
              .create(step)
              .then(param2 => {
                this.isSaving = false;
                this.$router.go(-1);
                const message = 'A Interface is created with identifier ' + param.id + ' for FunctionalFlow  ' + functionalFlowToSave.id;
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
            const message = 'A FlowInterface is created with identifier ' + param.id;
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

  public retrieveFlowInterface(flowInterfaceId): void {
    this.flowInterfaceService()
      .find(flowInterfaceId)
      .then(res => {
        this.flowInterface = res;
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
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
        this.assignSourceAndTarget();
      });
    this.applicationComponentService()
      .retrieve()
      .then(res => {
        this.applicationComponents = res.data;
      });
    this.protocolService()
      .retrieve()
      .then(res => {
        this.protocols = res.data;
        this.assignProtocol();
      });
    this.ownerService()
      .retrieve()
      .then(res => {
        this.owners = res.data;
      });
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
  }

  changeSource(component: IApplicationComponent) {
    this.flowInterface.source = component.application;
  }

  changeTarget(component: IApplicationComponent) {
    this.flowInterface.target = component.application;
  }
}
