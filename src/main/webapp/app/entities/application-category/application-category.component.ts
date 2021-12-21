import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IApplicationCategory } from '@/shared/model/application-category.model';

import ApplicationCategoryService from './application-category.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ApplicationCategory extends Vue {
  @Inject('applicationCategoryService') private applicationCategoryService: () => ApplicationCategoryService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  private removeId: number = null;

  public applicationCategories: IApplicationCategory[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllApplicationCategorys();
  }

  public clear(): void {
    this.retrieveAllApplicationCategorys();
  }

  public retrieveAllApplicationCategorys(): void {
    this.isFetching = true;
    this.applicationCategoryService()
      .retrieve()
      .then(
        res => {
          this.applicationCategories = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IApplicationCategory): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeApplicationCategory(): void {
    this.applicationCategoryService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A ApplicationCategory is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllApplicationCategorys();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
