import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDataFlowItem } from '@/shared/model/data-flow-item.model';
import DataFlowItemService from './data-flow-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DataFlowItemDetails extends Vue {
  @Inject('dataFlowItemService') private dataFlowItemService: () => DataFlowItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlowItem: IDataFlowItem = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowItemId) {
        vm.retrieveDataFlowItem(to.params.dataFlowItemId);
      }
    });
  }

  public retrieveDataFlowItem(dataFlowItemId) {
    this.dataFlowItemService()
      .find(dataFlowItemId)
      .then(res => {
        this.dataFlowItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
