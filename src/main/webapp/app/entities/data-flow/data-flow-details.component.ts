import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDataFlow } from '@/shared/model/data-flow.model';
import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DataFlowDetails extends Vue {
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlow: IDataFlow = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowId) {
        vm.retrieveDataFlow(to.params.dataFlowId);
      }
    });
  }

  public retrieveDataFlow(dataFlowId) {
    this.dataFlowService()
      .find(dataFlowId)
      .then(res => {
        this.dataFlow = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
