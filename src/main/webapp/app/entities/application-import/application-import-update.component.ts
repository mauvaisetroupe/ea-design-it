import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IApplicationImport, ApplicationImport } from '@/shared/model/application-import.model';
import ApplicationImportService from './application-import.service';
import { ImportStatus } from '@/shared/model/enumerations/import-status.model';

const validations: any = {
  applicationImport: {
    importId: {},
    excelFileName: {},
    idFromExcel: {},
    name: {},
    description: {},
    type: {},
    technology: {},
    comment: {},
    importStatus: {},
    importStatusMessage: {},
    existingApplicationID: {},
  },
};

@Component({
  validations,
})
export default class ApplicationImportUpdate extends Vue {
  @Inject('applicationImportService') private applicationImportService: () => ApplicationImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public applicationImport: IApplicationImport = new ApplicationImport();
  public importStatusValues: string[] = Object.keys(ImportStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationImportId) {
        vm.retrieveApplicationImport(to.params.applicationImportId);
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
    if (this.applicationImport.id) {
      this.applicationImportService()
        .update(this.applicationImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationImport is updated with identifier ' + param.id;
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
      this.applicationImportService()
        .create(this.applicationImport)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ApplicationImport is created with identifier ' + param.id;
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

  public retrieveApplicationImport(applicationImportId): void {
    this.applicationImportService()
      .find(applicationImportId)
      .then(res => {
        this.applicationImport = res;
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
