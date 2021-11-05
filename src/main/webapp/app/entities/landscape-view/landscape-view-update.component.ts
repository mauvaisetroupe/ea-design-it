import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';

import OwnerService from '@/entities/owner/owner.service';
import { IOwner } from '@/shared/model/owner.model';

import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import { ViewPoint } from '@/shared/model/enumerations/view-point.model';

const validations: any = {
  landscapeView: {
    viewpoint: {},
    diagramName: {},
    owner: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LandscapeViewUpdate extends Vue {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeView: ILandscapeView = new LandscapeView();

  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public functionalFlows: IFunctionalFlow[] = [];

  @Inject('ownerService') private ownerService: () => OwnerService;

  public owners: IOwner[] = [];
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
        this.landscapeView = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.functionalFlowService()
      .retrieve()
      .then(res => {
        this.functionalFlows = res.data;
      });
    this.ownerService()
      .retrieve()
      .then(res => {
        this.owners = res.data;
      });
  }
}
