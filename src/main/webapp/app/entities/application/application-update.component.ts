import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import { IApplicationCategory } from '@/shared/model/application-category.model';

import TechnologyService from '@/entities/technology/technology.service';
import { ITechnology } from '@/shared/model/technology.model';

import CapabilityService from '@/entities/capability/capability.service';
import { ICapability } from '@/shared/model/capability.model';

import ExternalReferenceService from '@/entities/external-reference/external-reference.service';
import { IExternalReference } from '@/shared/model/external-reference.model';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { IApplicationComponent } from '@/shared/model/application-component.model';

import { IApplication, Application } from '@/shared/model/application.model';
import ApplicationService from './application.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';

const validations: any = {
  application: {
    alias: {},
    name: {
      required,
    },
    description: {
      maxLength: maxLength(1500),
    },
    comment: {
      maxLength: maxLength(1000),
    },
    documentationURL: {
      maxLength: maxLength(500),
    },
    startDate: {},
    endDate: {},
    applicationType: {},
    softwareType: {},
    nickname: {},
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

  @Inject('applicationCategoryService') private applicationCategoryService: () => ApplicationCategoryService;

  public applicationCategories: IApplicationCategory[] = [];

  @Inject('technologyService') private technologyService: () => TechnologyService;

  public technologies: ITechnology[] = [];

  @Inject('capabilityService') private capabilityService: () => CapabilityService;

  public capabilities: ICapability[] = [];

  @Inject('externalReferenceService') private externalReferenceService: () => ExternalReferenceService;

  public externalReferences: IExternalReference[] = [];

  @Inject('applicationComponentService') private applicationComponentService: () => ApplicationComponentService;

  public applicationComponents: IApplicationComponent[] = [];
  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public softwareTypeValues: string[] = Object.keys(SoftwareType);
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
    this.application.categories = [];
    this.application.technologies = [];
    this.application.capabilities = [];
    this.application.externalIDS = [];
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
    this.capabilityService()
      .retrieve()
      .then(res => {
        this.capabilities = res.data;
      });
    this.externalReferenceService()
      .retrieve()
      .then(res => {
        this.externalReferences = res.data;
      });
    this.applicationComponentService()
      .retrieve()
      .then(res => {
        this.applicationComponents = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
