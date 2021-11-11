import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';
import { FlowImport, IFlowImport } from '@/shared/model/flow-import.model';

@Component
export default class FunctionalFlowDetails extends Vue {
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlow: IFunctionalFlow = {};
  public plantUMLImage = '';
  public captions: IFlowImport[] = [];

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.functionalFlowId) {
        vm.retrieveFunctionalFlow(to.params.functionalFlowId);
      }
    });
  }

  public retrieveFunctionalFlow(functionalFlowId) {
    this.functionalFlowService()
      .find(functionalFlowId)
      .then(res => {
        this.functionalFlow = res;
        this.fillCaption();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.getPlantUML(functionalFlowId);
  }

  public previousState() {
    this.$router.go(-1);
  }

  public fillCaption() {
    this.functionalFlow.interfaces.forEach(inter => {
      var caption: FlowImport = new FlowImport();
      caption.flowAlias = this.functionalFlow.alias;
      caption.idFlowFromExcel = inter.alias;
      caption.description = this.functionalFlow.description;
      caption.integrationPattern = inter.protocol;
      this.captions.push(caption);
    });
  }

  public getPlantUML(landscapeViewId) {
    console.log('Entering in method getPlantUML');
    this.functionalFlowService()
      .getPlantUML(landscapeViewId)
      .then(
        res => {
          this.plantUMLImage = 'data:image/jpg;base64,' + res.data;
        },
        err => {
          console.log(err);
        }
      );
  }
}
