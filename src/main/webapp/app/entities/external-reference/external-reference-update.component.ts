import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ExternalSystemService from '@/entities/external-system/external-system.service';
import { IExternalSystem } from '@/shared/model/external-system.model';

import ApplicationService from '@/entities/application/application.service';
import { IApplication } from '@/shared/model/application.model';

import { IExternalReference, ExternalReference } from '@/shared/model/external-reference.model';
import ExternalReferenceService from './external-reference.service';

const validations: any = {
  externalReference: {
    externalID: {},
  },
};

@Component({
  validations,
})
export default class ExternalReferenceUpdate extends Vue {
  @Inject('externalReferenceService') private externalReferenceService: () => ExternalReferenceService;
  @Inject('alertService') private alertService: () => AlertService;

  public externalReference: IExternalReference = new ExternalReference();

  @Inject('externalSystemService') private externalSystemService: () => ExternalSystemService;

  public externalSystems: IExternalSystem[] = [];

  @Inject('applicationService') private applicationService: () => ApplicationService;

  public applications: IApplication[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.externalReferenceId) {
        vm.retrieveExternalReference(to.params.externalReferenceId);
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
    if (this.externalReference.id) {
      this.externalReferenceService()
        .update(this.externalReference)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ExternalReference is updated with identifier ' + param.id;
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
      this.externalReferenceService()
        .create(this.externalReference)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ExternalReference is created with identifier ' + param.id;
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

  public retrieveExternalReference(externalReferenceId): void {
    this.externalReferenceService()
      .find(externalReferenceId)
      .then(res => {
        this.externalReference = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.externalSystemService()
      .retrieve()
      .then(res => {
        this.externalSystems = res.data;
      });
    this.applicationService()
      .retrieve()
      .then(res => {
        this.applications = res.data;
      });
  }
}
