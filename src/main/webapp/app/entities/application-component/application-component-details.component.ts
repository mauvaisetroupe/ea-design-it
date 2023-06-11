import { Component, Vue, Inject } from 'vue-property-decorator';

import { IApplicationComponent } from '@/shared/model/application-component.model';
import ApplicationComponentService from './application-component.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class ApplicationComponentDetails extends Vue {
  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') public accountService: () => AccountService;
  public applicationComponent: IApplicationComponent = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationComponentId) {
        vm.retrieveApplicationComponent(to.params.applicationComponentId);
      }
    });
  }

  public retrieveApplicationComponent(applicationComponentId) {
    this.applicationComponentService()
      .find(applicationComponentId)
      .then(res => {
        this.applicationComponent = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
