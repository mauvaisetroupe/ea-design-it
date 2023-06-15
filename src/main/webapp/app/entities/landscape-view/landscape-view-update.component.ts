import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import CapabilityApplicationMappingService from '@/entities/capability-application-mapping/capability-application-mapping.service';
import { ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';

import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import { ViewPoint } from '@/shared/model/enumerations/view-point.model';

const validations: any = {
  landscapeView: {
    viewpoint: {},
    diagramName: {},
    compressedDrawXML: {},
    compressedDrawSVG: {},
  },
};

@Component({
  validations,
})
export default class LandscapeViewUpdate extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeView: ILandscapeView = new LandscapeView();

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];

  @Inject('capabilityApplicationMappingService') private capabilityApplicationMappingService: () => CapabilityApplicationMappingService;

  public capabilityApplicationMappings: ICapabilityApplicationMapping[] = [];
  public viewPointValues: string[] = Object.keys(ViewPoint);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeViewId) {
        vm.retrieveLandscapeView(to.params.landscapeViewId);
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
    this.landscapeView.flows = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.landscapeView.id) {
      this.landscapeViewService()
        .update(this.landscapeView)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A LandscapeView is updated with identifier ' + param.id;
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
      this.landscapeViewService()
        .create(this.landscapeView)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A LandscapeView is created with identifier ' + param.id;
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

  public retrieveLandscapeView(landscapeViewId): void {
    this.landscapeViewService()
      .find(landscapeViewId)
      .then(res => {
        this.landscapeView = res.landscape;
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
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
    this.capabilityApplicationMappingService()
      .retrieve()
      .then(res => {
        this.capabilityApplicationMappings = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
