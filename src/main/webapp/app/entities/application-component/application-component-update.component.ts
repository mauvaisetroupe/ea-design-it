import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import { IApplicationCategory } from '@/shared/model/application-category.model';

import TechnologyService from '@/entities/technology/technology.service';
import { ITechnology } from '@/shared/model/technology.model';

import { IApplicationComponent, ApplicationComponent } from '@/shared/model/application-component.model';
import ApplicationComponentService from './application-component.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';

const validations: any = {
  applicationComponent: {
    alias: {},
    name: {
      required,
    },
    description: {
      maxLength: maxLength(1000),
    },
    comment: {
      maxLength: maxLength(500),
    },
    documentationURL: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
    applicationType: {},
    softwareType: {},
    displayInLandscape: {},
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

  @Inject('applicationCategoryService') private applicationCategoryService: () => ApplicationCategoryService;

  public applicationCategories: IApplicationCategory[] = [];

  @Inject('technologyService') private technologyService: () => TechnologyService;

  public technologies: ITechnology[] = [];
  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public softwareTypeValues: string[] = Object.keys(SoftwareType);
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
    this.applicationComponent.categories = [];
    this.applicationComponent.technologies = [];
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
    this.applicationCategoryService()
      .retrieve()
      .then(res => {
        this.applicationCategories = res.data;
      });
    this.technologyService()
      .retrieve()
      .then(res => {
        this.technologies = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
