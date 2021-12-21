import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITechnology } from '@/shared/model/technology.model';
import TechnologyService from './technology.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class TechnologyDetails extends Vue {
  @Inject('technologyService') private technologyService: () => TechnologyService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  public technology: ITechnology = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.technologyId) {
        vm.retrieveTechnology(to.params.technologyId);
      }
    });
  }

  public retrieveTechnology(technologyId) {
    this.technologyService()
      .find(technologyId)
      .then(res => {
        this.technology = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
