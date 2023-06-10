import { mixins } from 'vue-class-component';
import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ILandscapeView } from '@/shared/model/landscape-view.model';

import JhiDataUtils from '@/shared/data/data-utils.service';

import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class LandscapeView extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') public accountService: () => AccountService;
  private removeId: number = null;

  public landscapeViews: ILandscapeView[] = [];

  public deleteFunctionalFlows = true;
  public deleteInterfaces = true;
  public deleteDatas = true;

  public isFetching = false;

  public deleteCoherence() {
    if (!this.deleteFunctionalFlows) {
      this.deleteInterfaces = false;
      this.deleteDatas = false;
    }
    if (!this.deleteInterfaces) {
      this.deleteDatas = false;
    }
  }

  public mounted(): void {
    this.retrieveAllLandscapeViews();
  }

  public clear(): void {
    this.retrieveAllLandscapeViews();
  }

  public retrieveAllLandscapeViews(): void {
    this.isFetching = true;
    this.landscapeViewService()
      .retrieve()
      .then(
        res => {
          this.landscapeViews = res.data;
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

  public prepareRemove(instance: ILandscapeView): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeLandscapeView(): void {
    this.landscapeViewService()
      .delete(this.removeId, this.deleteFunctionalFlows, this.deleteInterfaces, this.deleteDatas)
      .then(() => {
        const message = 'A LandscapeView is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllLandscapeViews();
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
