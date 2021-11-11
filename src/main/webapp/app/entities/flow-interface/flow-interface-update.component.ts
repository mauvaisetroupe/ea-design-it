import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import { IFlowInterface, FlowInterface } from '@/shared/model/flow-interface.model';
import FlowInterfaceService from './flow-interface.service';
import { Protocol } from '@/shared/model/enumerations/protocol.model';

const validations: any = {
  flowInterface: {
    alias: {},
    protocol: {},
    status: {},
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

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];
  public protocolValues: string[] = Object.keys(Protocol);
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
      });
    this.applicationComponentService()
      .retrieve()
      .then(res => {
        this.applicationComponents = res.data;
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
}
