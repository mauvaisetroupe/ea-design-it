import { Component, Vue, Inject } from 'vue-property-decorator';

import { IExternalSystem } from '@/shared/model/external-system.model';
import ExternalSystemService from './external-system.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ExternalSystemDetails extends Vue {
  @Inject('externalSystemService') private externalSystemService: () => ExternalSystemService;
  @Inject('alertService') private alertService: () => AlertService;

  public externalSystem: IExternalSystem = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.externalSystemId) {
        vm.retrieveExternalSystem(to.params.externalSystemId);
      }
    });
  }

  public retrieveExternalSystem(externalSystemId) {
    this.externalSystemService()
      .find(externalSystemId)
      .then(res => {
        this.externalSystem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
