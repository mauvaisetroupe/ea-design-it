import { Component, Vue, Inject } from 'vue-property-decorator';

import { IApplicationCategory } from '@/shared/model/application-category.model';
import ApplicationCategoryService from './application-category.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component
export default class ApplicationCategoryDetails extends Vue {
  @Inject('applicationCategoryService') private applicationCategoryService: () => ApplicationCategoryService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') public accountService: () => AccountService;
  public applicationCategory: IApplicationCategory = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationCategoryId) {
        vm.retrieveApplicationCategory(to.params.applicationCategoryId);
      }
    });
  }

  public retrieveApplicationCategory(applicationCategoryId) {
    this.applicationCategoryService()
      .find(applicationCategoryId)
      .then(res => {
        this.applicationCategory = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
