import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';
import ProtocolService from '@/entities/protocol/protocol.service';
import { IProtocol } from '@/shared/model/protocol.model';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';
import { IFunctionalFlowStep, FunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';

@Component
export default class FunctionalFlowDetails extends Vue {
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  @Inject('applicationService') private applicationService: () => ApplicationService;
  public applications: IApplication[] = [];

  @Inject('protocolService') private protocolService: () => ProtocolService;
  public protocols: IProtocol[] = [];

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  public interfaces: IFlowInterface[] = [];

  public checkedInterface: IFlowInterface[] = [];

  get searchSourceId() {
    if (!this.searchSourceName) return null;
    return this.applications.find(i => i.name == this.searchSourceName).id;
  }

  get searchTargetId() {
    if (!this.searchTargetName) return null;
    return this.applications.find(i => i.name == this.searchTargetName).id;
  }

  get searchProtocolId() {
    if (!this.searchProtocolName) return null;
    return this.protocols.find(i => i.name == this.searchProtocolName).id;
  }

  public functionalFlow: IFunctionalFlow = {};
  public plantUMLImage = '';

  public searchSourceName = '';
  public searchTargetName = '';
  public searchProtocolName = '';

  public toBeSaved: boolean = false;
  public searchDone = false;

  public indexStepToDetach: number;

  public notPersisted: boolean = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowId) {
        if (to.params.functionalFlowId != 'new') {
          vm.retrieveFunctionalFlow(to.params.functionalFlowId);
        } else if (to.query.id) {
          vm.generateDiagramForSelection(to.query.id);
        }
      }
    });
  }

  public generateDiagramForSelection(applicationIds: number[]) {
    this.notPersisted = true;
    this.functionalFlowService()
      .createNewFromApplications(applicationIds)
      .then(res => {
        this.functionalFlow = res;
        this.getPlantUMLforapplications(applicationIds);
      });
  }

  public retrieveFunctionalFlow(functionalFlowId) {
    this.notPersisted = false;
    this.functionalFlowService()
      .find(functionalFlowId)
      .then(res => {
        this.functionalFlow = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.getPlantUML(functionalFlowId);
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getPlantUML(landscapeViewId) {
    console.log('Entering in method getPlantUML');
    this.functionalFlowService()
      .getPlantUML(landscapeViewId)
      .then(
        res => {
          this.plantUMLImage = res.data;
        },
        err => {
          console.log(err);
        }
      );
  }

  public getPlantUMLforapplications(aplicationIds: number[]) {
    console.log('Entering in method getPlantUMLforapplications');
    this.functionalFlowService()
      .getPlantUMLforApplications(aplicationIds)
      .then(
        res => {
          console.log(res.data);

          this.plantUMLImage = res.data;
        },
        err => {
          console.log(err);
        }
      );
  }
  public prepareSearchInterfaces(): void {
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
      });
    this.protocolService()
      .retrieve()
      .then(res => {
        this.protocols = res.data;
      });

    if (<any>this.$refs.searchEntity) {
      (<any>this.$refs.searchEntity).show();
    }
  }

  public searchInterfaces() {
    this.searchDone = false;
    this.checkedInterface = [];
    this.flowInterfaceService()
      .search(this.searchSourceId, this.searchTargetId, this.searchProtocolId)
      .then(res => {
        this.interfaces = res.data;
        this.searchDone = true;
      });
  }

  public addOrCreateInterface(): void {
    let maxStepOrder: number = this.functionalFlow.steps?.length > 0 ? Math.max(...this.functionalFlow.steps.map(o => o.stepOrder)) : 0;
    if (this.checkedInterface && this.checkedInterface.length > 0) {
      if (!this.functionalFlow.steps) {
        this.functionalFlow.steps = [];
      }
      this.checkedInterface.forEach(inter => {
        maxStepOrder = maxStepOrder + 1;
        let step: IFunctionalFlowStep = new FunctionalFlowStep();
        step.flowInterface = inter;
        step.flow = this.functionalFlow;
        step.stepOrder = maxStepOrder;
        this.functionalFlowStepService()
          .create(step)
          .then(res => {
            this.retrieveFunctionalFlow(this.functionalFlow.id);
            this.closeSearchDialog();
            this.toBeSaved = false;
            //this.getPlantUML(this.functionalFlow.id);
          });
      });
      this.toBeSaved = true;
    }
  }

  public closeSearchDialog(): void {
    (<any>this.$refs.searchEntity).hide();
  }

  public detachInterface() {
    const stepToDelete: FunctionalFlowStep = this.functionalFlow.steps[this.indexStepToDetach];
    console.log('about to delete : ' + stepToDelete.id);
    this.functionalFlowStepService()
      .delete(stepToDelete.id)
      .then(res => {
        this.retrieveFunctionalFlow(this.functionalFlow.id);
        this.closeDetachDialog();
        this.toBeSaved = false;
      });
  }

  public prepareToDetach(index: number) {
    console.log(index);
    if (<any>this.$refs.detachInterfaceEntity) {
      (<any>this.$refs.detachInterfaceEntity).show();
    }
    this.indexStepToDetach = index;
  }

  public closeDetachDialog(): void {
    (<any>this.$refs.detachInterfaceEntity).hide();
  }
}
