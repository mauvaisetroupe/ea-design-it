import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IFlowImport, FlowImport } from '@/shared/model/flow-import.model';
import FlowImportService from './flow-import.service';
import { ImportStatus } from '@/shared/model/enumerations/import-status.model';

const validations: any = {
  flowImport: {
    idFlowFromExcel: {},
    flowAlias: {},
    sourceElement: {},
    targetElement: {},
    description: {},
    stepDescription: {},
    integrationPattern: {},
    frequency: {},
    format: {},
    swagger: {},
    sourceURLDocumentation: {},
    targetURLDocumentation: {},
    sourceDocumentationStatus: {},
    targetDocumentationStatus: {},
    flowStatus: {},
    comment: {},
    documentName: {},
    importInterfaceStatus: {},
    importFunctionalFlowStatus: {},
    importDataFlowStatus: {},
    importStatusMessage: {},
  },
};

@Component({
  validations,
})
export default class FlowImportUpdate extends Vue {
  @Inject('flowImportService') private flowImportService: () => FlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public flowImport: IFlowImport = new FlowImport();
  public importStatusValues: string[] = Object.keys(ImportStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.flowImportId) {
        vm.retrieveFlowImport(to.params.flowImportId);
      }
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
    if (this.flowImport.id) {
      this.flowImportService()
        .update(this.flowImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FlowImport is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
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
      this.flowImportService()
        .create(this.flowImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A FlowImport is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
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

  public retrieveFlowImport(flowImportId): void {
    this.flowImportService()
      .find(flowImportId)
      .then(res => {
        this.flowImport = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
