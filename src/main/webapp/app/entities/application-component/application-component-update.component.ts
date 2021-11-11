import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import { IApplicationComponent, ApplicationComponent } from '@/shared/model/application-component.model';
import ApplicationComponentService from './application-component.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';

const validations: any = {
  applicationComponent: {
    name: {},
    description: {
      maxLength: maxLength(1000),
    },
    type: {},
    technology: {},
    comment: {},
    application: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ApplicationComponentUpdate extends Vue {
  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;
  @Inject('alertService') private alertService: () => AlertService;

  public applicationComponent: IApplicationComponent = new ApplicationComponent();

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];
  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationComponentId) {
        vm.retrieveApplicationComponent(to.params.applicationComponentId);
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
    if (this.applicationComponent.id) {
      this.applicationComponentService()
        .update(this.applicationComponent)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationComponent is updated with identifier ' + param.id;
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
      this.applicationComponentService()
        .create(this.applicationComponent)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationComponent is created with identifier ' + param.id;
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

  public retrieveApplicationComponent(applicationComponentId): void {
    this.applicationComponentService()
      .find(applicationComponentId)
      .then(res => {
        this.applicationComponent = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
      });
  }
}