import { Component, Vue, Inject, Watch } from 'vue-property-decorator';

import { FunctionalFlow, IFunctionalFlow } from '@/shared/model/functional-flow.model';
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
  @Inject('accountService') public accountService: () => AccountService;

  @Inject('applicationService') private applicationService: () => ApplicationService;
  public applications: IApplication[] = [];

  @Inject('protocolService') private protocolService: () => ProtocolService;
  public protocols: IProtocol[] = [];

  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  public interfaces: IFlowInterface[] = [];

  public checkedInterface: IFlowInterface[] = [];

  public sequenceDiagram = true;

  public functionalFlow: IFunctionalFlow = {};
  public plantUMLImage = '';

  public searchSourceName = '';
  public searchTargetName = '';
  public searchProtocolName = '';

  public toBeSaved = false;
  public searchDone = false;

  public indexStepToDetach: number;

  public isFetching = false;

  public applicationIds = [];

  //////////////////////////////
  // Store current tab in sessionStorage
  //////////////////////////////

  public tabIndex = 0;
  public flowId = -1;
  public sessionKey = 'flow.detail.tab';

  @Watch('tabIndex')
  public onTabChange(newtab) {
    if (this.functionalFlow && this.functionalFlow.id) {
      this.tabIndex = newtab;
      sessionStorage.setItem(this.sessionKey, this.functionalFlow.id + '#' + this.tabIndex);
    }
  }

  public created() {
    // https://github.com/bootstrap-vue/bootstrap-vue/issues/2803
    this.$nextTick(() => {
      this.loadTab(this.flowId);
    });
  }

  public loadTab(_landscapeID) {
    if (sessionStorage.getItem(this.sessionKey)) {
      const parts = sessionStorage.getItem(this.sessionKey).split('#');
      const landId = parseInt(parts[0]);
      const tabIndex = parseInt(parts[1]);
      const landscapeID = parseInt(_landscapeID);
      if (landscapeID === landId) {
        this.tabIndex = tabIndex;
      } else {
        sessionStorage.removeItem(this.sessionKey);
      }
    } else {
      this.tabIndex = 1;
    }
  }

  //for description update
  public reorderAliasflowToSave: IFunctionalFlow[] = [];
  //for reordering update
  public reorderStepToSave: IFunctionalFlowStep[] = [];

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowId) {
        vm.retrieveFunctionalFlow(to.params.functionalFlowId);
        vm.flowId = parseInt(to.params.functionalFlowId);
      }
    });
  }

  public changeDiagram() {
    this.isFetching = true;
    this.sequenceDiagram = !this.sequenceDiagram;
    this.getPlantUML(this.functionalFlow.id);
  }

  public retrieveFunctionalFlow(functionalFlowId) {
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

  public getPlantUML(functionalFlowId) {
    console.log('Entering in method getPlantUML');
    this.functionalFlowService()
      .getPlantUML(functionalFlowId, this.sequenceDiagram)
      .then(
        res => {
          this.plantUMLImage = res.data;
          this.isFetching = false;
        },
        err => {
          console.log(err);
        }
      );
  }

  public exportPlantUML() {
    this.functionalFlowService()
      .getPlantUMLSource(this.functionalFlow.id, this.sequenceDiagram)
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'text/plain',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', this.functionalFlow.alias + '-plantuml.txt');
        document.body.appendChild(link);
        link.click();
      });
  }
}
