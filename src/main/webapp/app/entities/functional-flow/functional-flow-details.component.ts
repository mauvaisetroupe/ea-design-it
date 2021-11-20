import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FunctionalFlowDetails extends Vue {
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public functionalFlow: IFunctionalFlow = {};
  public plantUMLImage = '';
  public captions = [];

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
      var caption = {
        flowAlias: this.functionalFlow.alias,
        interfaceAlias: inter.alias,
        interfaceID: inter.id,
        description: this.functionalFlow.description,
        protocol: inter.protocol,
        source: inter.source.name,
        target: inter.target.name,
        dataFlows: inter.dataFlows,
      };
      this.captions.push(caption);
    });
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
}
