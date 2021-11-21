import { Component, Vue, Inject } from 'vue-property-decorator';

import { IApplication } from '@/shared/model/application.model';
import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ApplicationDetails extends Vue {
  @Inject('applicationService') private applicationService: () => ApplicationService;
  @Inject('alertService') private alertService: () => AlertService;

  public application: IApplication = {};
  public plantUMLImage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.applicationId) {
        vm.retrieveApplication(to.params.applicationId);
      }
    });
  }

  public retrieveApplication(applicationId) {
    this.applicationService()
      .find(applicationId)
      .then(res => {
        this.application = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.getPlantUML(applicationId);
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getPlantUML(applicationId) {
    this.applicationService()
      .getPlantUML(applicationId)
      .then(
        res => {
          this.plantUMLImage = res.data;
        },
        err => {
          console.log(err);
        }
      );
  }
}
