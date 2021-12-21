import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFlowInterface } from '@/shared/model/flow-interface.model';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class FlowInterfaceDetails extends Vue {
  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  public flowInterface: IFlowInterface = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowInterfaceId) {
        vm.retrieveFlowInterface(to.params.flowInterfaceId);
      }
    });
  }

  public retrieveFlowInterface(flowInterfaceId) {
    this.flowInterfaceService()
      .find(flowInterfaceId)
      .then(res => {
        this.flowInterface = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
