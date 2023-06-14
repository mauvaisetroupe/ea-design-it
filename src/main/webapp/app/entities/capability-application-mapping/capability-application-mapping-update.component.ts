import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import CapabilityService from '@/entities/capability/capability.service';
import { ICapability } from '@/shared/model/capability.model';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import { ILandscapeView } from '@/shared/model/landscape-view.model';

import { ICapabilityApplicationMapping, CapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';
import CapabilityApplicationMappingService from './capability-application-mapping.service';

const validations: any = {
  capabilityApplicationMapping: {},
};

@Component({
  validations,
})
export default class CapabilityApplicationMappingUpdate extends Vue {
  @Inject('capabilityApplicationMappingService') private capabilityApplicationMappingService: () => CapabilityApplicationMappingService;
  @Inject('alertService') private alertService: () => AlertService;

  public capabilityApplicationMapping: ICapabilityApplicationMapping = new CapabilityApplicationMapping();

  @Inject('capabilityService') private capabilityService: () => CapabilityService;

  public capabilities: ICapability[] = [];

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];

  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;

  public landscapeViews: ILandscapeView[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.capabilityApplicationMappingId) {
        vm.retrieveCapabilityApplicationMapping(to.params.capabilityApplicationMappingId);
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
    this.capabilityApplicationMapping.landscapes = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.capabilityApplicationMapping.id) {
      this.capabilityApplicationMappingService()
        .update(this.capabilityApplicationMapping)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A CapabilityApplicationMapping is updated with identifier ' + param.id;
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
      this.capabilityApplicationMappingService()
        .create(this.capabilityApplicationMapping)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A CapabilityApplicationMapping is created with identifier ' + param.id;
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

  public retrieveCapabilityApplicationMapping(capabilityApplicationMappingId): void {
    this.capabilityApplicationMappingService()
      .find(capabilityApplicationMappingId)
      .then(res => {
        this.capabilityApplicationMapping = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.capabilityService()
      .retrieve()
      .then(res => {
        this.capabilities = res.data;
      });
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
      });
    this.landscapeViewService()
      .retrieve()
      .then(res => {
        this.landscapeViews = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
