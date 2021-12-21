import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ITechnology } from '@/shared/model/technology.model';

import TechnologyService from './technology.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Technology extends Vue {
  @Inject('technologyService') private technologyService: () => TechnologyService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  private removeId: number = null;

  public technologies: ITechnology[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllTechnologys();
  }

  public clear(): void {
    this.retrieveAllTechnologys();
  }

  public retrieveAllTechnologys(): void {
    this.isFetching = true;
    this.technologyService()
      .retrieve()
      .then(
        res => {
          this.technologies = res.data;
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

  public prepareRemove(instance: ITechnology): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeTechnology(): void {
    this.technologyService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Technology is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllTechnologys();
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
