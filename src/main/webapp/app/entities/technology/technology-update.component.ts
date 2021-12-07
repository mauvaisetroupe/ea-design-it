import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { ITechnology, Technology } from '@/shared/model/technology.model';
import TechnologyService from './technology.service';

const validations: any = {
  technology: {
    name: {},
    type: {},
    description: {
      maxLength: maxLength(250),
    },
  },
};

@Component({
  validations,
})
export default class TechnologyUpdate extends Vue {
  @Inject('technologyService') private technologyService: () => TechnologyService;
  @Inject('alertService') private alertService: () => AlertService;

  public technology: ITechnology = new Technology();

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.technologyId) {
        vm.retrieveTechnology(to.params.technologyId);
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
    if (this.technology.id) {
      this.technologyService()
        .update(this.technology)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Technology is updated with identifier ' + param.id;
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
      this.technologyService()
        .create(this.technology)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Technology is created with identifier ' + param.id;
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

  public retrieveTechnology(technologyId): void {
    this.technologyService()
      .find(technologyId)
      .then(res => {
        this.technology = res;
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
    this.applicationComponentService()
      .retrieve()
      .then(res => {
        this.applicationComponents = res.data;
      });
  }
}
