import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { IApplication, Application } from '@/shared/model/application.model';
import ApplicationService from './application.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';

const validations: any = {
  application: {
    name: {},
    description: {},
    type: {},
    technology: {},
    comment: {},
  },
};

@Component({
  validations,
})
export default class ApplicationUpdate extends Vue {
  @Inject('applicationService') private applicationService: () => ApplicationService;
  @Inject('alertService') private alertService: () => AlertService;

  public application: IApplication = new Application();

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];
  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationId) {
        vm.retrieveApplication(to.params.applicationId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.application.id) {
      this.applicationService()
        .update(this.application)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Application is updated with identifier ' + param.id;
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
      this.applicationService()
        .create(this.application)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Application is created with identifier ' + param.id;
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

  public retrieveApplication(applicationId): void {
    this.applicationService()
      .find(applicationId)
      .then(res => {
        this.application = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.ownerService()
      .retrieve()
      .then(res => {
        this.owners = res.data;
      });
    this.applicationComponentService()
      .retrieve()
      .then(res => {
        this.applicationComponents = res.data;
      });
  }
}
