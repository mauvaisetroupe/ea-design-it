import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import { IOwner, Owner } from '@/shared/model/owner.model';
import OwnerService from './owner.service';

const validations: any = {
  owner: {
    name: {},
  },
};

@Component({
  validations,
})
export default class OwnerUpdate extends Vue {
  @Inject('ownerService') private ownerService: () => OwnerService;
  @Inject('alertService') private alertService: () => AlertService;

  public owner: IOwner = new Owner();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.ownerId) {
        vm.retrieveOwner(to.params.ownerId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.owner.users = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.owner.id) {
      this.ownerService()
        .update(this.owner)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Owner is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.ownerService()
        .create(this.owner)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Owner is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveOwner(ownerId): void {
    this.ownerService()
      .find(ownerId)
      .then(res => {
        this.owner = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
