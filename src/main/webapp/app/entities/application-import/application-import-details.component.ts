import { Component, Vue, Inject } from 'vue-property-decorator';

import { IApplicationImport } from '@/shared/model/application-import.model';
import ApplicationImportService from './application-import.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ApplicationImportDetails extends Vue {
  @Inject('applicationImportService') private applicationImportService: () => ApplicationImportService;
  @Inject('alertService') private alertService: () => AlertService;

  public applicationImport: IApplicationImport = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationImportId) {
        vm.retrieveApplicationImport(to.params.applicationImportId);
      }
    });
  }

  public retrieveApplicationImport(applicationImportId) {
    this.applicationImportService()
      .find(applicationImportId)
      .then(res => {
        this.applicationImport = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
