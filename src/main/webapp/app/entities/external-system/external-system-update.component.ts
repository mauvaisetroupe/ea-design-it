import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IExternalSystem, ExternalSystem } from '@/shared/model/external-system.model';
import ExternalSystemService from './external-system.service';

const validations: any = {
  externalSystem: {
    externalSystemID: {},
  },
};

@Component({
  validations,
})
export default class ExternalSystemUpdate extends Vue {
  @Inject('externalSystemService') private externalSystemService: () => ExternalSystemService;
  @Inject('alertService') private alertService: () => AlertService;

  public externalSystem: IExternalSystem = new ExternalSystem();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.externalSystemId) {
        vm.retrieveExternalSystem(to.params.externalSystemId);
      }
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.externalSystem.id) {
      this.externalSystemService()
        .update(this.externalSystem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ExternalSystem is updated with identifier ' + param.id;
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
      this.externalSystemService()
        .create(this.externalSystem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ExternalSystem is created with identifier ' + param.id;
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

  public retrieveExternalSystem(externalSystemId): void {
    this.externalSystemService()
      .find(externalSystemId)
      .then(res => {
        this.externalSystem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
