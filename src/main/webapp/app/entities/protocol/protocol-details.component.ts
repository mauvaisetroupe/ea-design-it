import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProtocol } from '@/shared/model/protocol.model';
import ProtocolService from './protocol.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class ProtocolDetails extends Vue {
  @Inject('protocolService') private protocolService: () => ProtocolService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  public protocol: IProtocol = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.protocolId) {
        vm.retrieveProtocol(to.params.protocolId);
      }
    });
  }

  public retrieveProtocol(protocolId) {
    this.protocolService()
      .find(protocolId)
      .then(res => {
        this.protocol = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
