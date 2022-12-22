import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFlowGroup } from '@/shared/model/flow-group.model';
import FlowGroupService from './flow-group.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FlowGroupDetails extends Vue {
  @Inject('flowGroupService') private flowGroupService: () => FlowGroupService;
  @Inject('alertService') private alertService: () => AlertService;

  public flowGroup: IFlowGroup = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowGroupId) {
        vm.retrieveFlowGroup(to.params.flowGroupId);
      }
    });
  }

  public retrieveFlowGroup(flowGroupId) {
    this.flowGroupService()
      .find(flowGroupId)
      .then(res => {
        this.flowGroup = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
