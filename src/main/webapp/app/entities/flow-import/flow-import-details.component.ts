import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFlowImport } from '@/shared/model/flow-import.model';
import FlowImportService from './flow-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FlowImportDetails extends Vue {
  @Inject('flowImportService') private flowImportService: () => FlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public flowImport: IFlowImport = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowImportId) {
        vm.retrieveFlowImport(to.params.flowImportId);
      }
    });
  }

  public retrieveFlowImport(flowImportId) {
    this.flowImportService()
      .find(flowImportId)
      .then(res => {
        this.flowImport = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
