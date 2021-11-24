import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDataFlowImport } from '@/shared/model/data-flow-import.model';
import DataFlowImportService from './data-flow-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DataFlowImportDetails extends Vue {
  @Inject('dataFlowImportService') private dataFlowImportService: () => DataFlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlowImport: IDataFlowImport = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowImportId) {
        vm.retrieveDataFlowImport(to.params.dataFlowImportId);
      }
    });
  }

  public retrieveDataFlowImport(dataFlowImportId) {
    this.dataFlowImportService()
      .find(dataFlowImportId)
      .then(res => {
        this.dataFlowImport = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
