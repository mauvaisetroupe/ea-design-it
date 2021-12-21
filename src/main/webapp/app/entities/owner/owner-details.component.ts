import { Component, Vue, Inject } from 'vue-property-decorator';

import { IOwner } from '@/shared/model/owner.model';
import OwnerService from './owner.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class OwnerDetails extends Vue {
  @Inject('ownerService') private ownerService: () => OwnerService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  public owner: IOwner = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ownerId) {
        vm.retrieveOwner(to.params.ownerId);
      }
    });
  }

  public retrieveOwner(ownerId) {
    this.ownerService()
      .find(ownerId)
      .then(res => {
        this.owner = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
