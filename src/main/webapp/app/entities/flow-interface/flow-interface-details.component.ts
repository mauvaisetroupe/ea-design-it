import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFlowInterface } from '@/shared/model/flow-interface.model';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import DataFlowService from '@/entities/data-flow/data-flow.service';
import { IDataFlow } from '@/shared/model/data-flow.model';

@Component
export default class FlowInterfaceDetails extends Vue {
  @Inject('flowInterfaceService') private flowInterfaceService: () => FlowInterfaceService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('dataFlowService') private dataFlowService: () => DataFlowService;

  public flowInterface: IFlowInterface = {};
  public dataFlowToDetach: IDataFlow;

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

  public prepareToDetach(dataFlow: IDataFlow) {
    if (<any>this.$refs.detachDataEntity) {
      (<any>this.$refs.detachDataEntity).show();
    }
    this.dataFlowToDetach = dataFlow;
  }

  public detachDataFlow() {
    this.dataFlowService()
      .update(this.dataFlowToDetach)
      .then(res => {
        this.retrieveFlowInterface(this.flowInterface.id);
        this.closeDetachDialog();
      });
  }

  public closeDetachDialog(): void {
    (<any>this.$refs.detachDataEntity).hide();
  }

  public isOwner(flowInterface: IFlowInterface): Boolean {
    const username = this.$store.getters.account?.login ?? '';
    if (this.accountService().writeAuthorities) {
      return true;
    }
    if (flowInterface.owner && flowInterface.owner.users) {
      for (const user of flowInterface.owner.users) {
        if (user.login === username) {
          return true;
        }
      }
    }
    return false;
  }
}
