import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IDataFlowImport, DataFlowImport } from '@/shared/model/data-flow-import.model';
import DataFlowImportService from './data-flow-import.service';
import { ImportStatus } from '@/shared/model/enumerations/import-status.model';

const validations: any = {
  dataFlowImport: {
    dataId: {},
    dataParentId: {},
    dataParentName: {},
    functionalFlowId: {},
    flowInterfaceId: {},
    dataType: {},
    dataResourceName: {},
    dataResourceType: {},
    dataDescription: {},
    dataFrequency: {},
    dataFormat: {},
    dataContractURL: {},
    dataDocumentationURL: {},
    source: {},
    target: {},
    importDataStatus: {},
    importDataItemStatus: {},
    importStatusMessage: {},
  },
};

@Component({
  validations,
})
export default class DataFlowImportUpdate extends Vue {
  @Inject('dataFlowImportService') private dataFlowImportService: () => DataFlowImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public dataFlowImport: IDataFlowImport = new DataFlowImport();
  public importStatusValues: string[] = Object.keys(ImportStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dataFlowImportId) {
        vm.retrieveDataFlowImport(to.params.dataFlowImportId);
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
    if (this.dataFlowImport.id) {
      this.dataFlowImportService()
        .update(this.dataFlowImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlowImport is updated with identifier ' + param.id;
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
      this.dataFlowImportService()
        .create(this.dataFlowImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DataFlowImport is created with identifier ' + param.id;
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

  public retrieveDataFlowImport(dataFlowImportId): void {
    this.dataFlowImportService()
      .find(dataFlowImportId)
      .then(res => {
        this.dataFlowImport = res;
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
