import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { IApplicationCategory, ApplicationCategory } from '@/shared/model/application-category.model';
import ApplicationCategoryService from './application-category.service';

const validations: any = {
  applicationCategory: {
    name: {},
    type: {},
    description: {},
  },
};

@Component({
  validations,
})
export default class ApplicationCategoryUpdate extends Vue {
  @Inject('applicationCategoryService') private applicationCategoryService: () => ApplicationCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public applicationCategory: IApplicationCategory = new ApplicationCategory();

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationCategoryId) {
        vm.retrieveApplicationCategory(to.params.applicationCategoryId);
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
    if (this.applicationCategory.id) {
      this.applicationCategoryService()
        .update(this.applicationCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationCategory is updated with identifier ' + param.id;
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
      this.applicationCategoryService()
        .create(this.applicationCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationCategory is created with identifier ' + param.id;
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

  public retrieveApplicationCategory(applicationCategoryId): void {
    this.applicationCategoryService()
      .find(applicationCategoryId)
      .then(res => {
        this.applicationCategory = res;
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
