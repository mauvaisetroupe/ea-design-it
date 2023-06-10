import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IOwner } from '@/shared/model/owner.model';

import OwnerService from './owner.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Owner extends Vue {
  @Inject('ownerService') private ownerService: () => OwnerService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') public accountService: () => AccountService;
  private removeId: number = null;

  public owners: IOwner[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllOwners();
  }

  public clear(): void {
    this.retrieveAllOwners();
  }

  public retrieveAllOwners(): void {
    this.isFetching = true;
    this.ownerService()
      .retrieve()
      .then(
        res => {
          this.owners = res.data;
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

  public prepareRemove(instance: IOwner): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeOwner(): void {
    this.ownerService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Owner is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllOwners();
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
