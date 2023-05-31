import { Component, Vue, Inject } from 'vue-property-decorator';

import { IExternalReference } from '@/shared/model/external-reference.model';
import ExternalReferenceService from './external-reference.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ExternalReferenceDetails extends Vue {
  @Inject('externalReferenceService') private externalReferenceService: () => ExternalReferenceService;
  @Inject('alertService') private alertService: () => AlertService;

  public externalReference: IExternalReference = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.externalReferenceId) {
        vm.retrieveExternalReference(to.params.externalReferenceId);
      }
    });
  }

  public retrieveExternalReference(externalReferenceId) {
    this.externalReferenceService()
      .find(externalReferenceId)
      .then(res => {
        this.externalReference = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
