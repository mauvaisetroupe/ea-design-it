import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';
import CapabilityApplicationMappingService from './capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CapabilityApplicationMappingDetails extends Vue {
  @Inject('capabilityApplicationMappingService') private capabilityApplicationMappingService: () => CapabilityApplicationMappingService;
  @Inject('alertService') private alertService: () => AlertService;

  public capabilityApplicationMapping: ICapabilityApplicationMapping = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.capabilityApplicationMappingId) {
        vm.retrieveCapabilityApplicationMapping(to.params.capabilityApplicationMappingId);
      }
    });
  }

  public retrieveCapabilityApplicationMapping(capabilityApplicationMappingId) {
    this.capabilityApplicationMappingService()
      .find(capabilityApplicationMappingId)
      .then(res => {
        this.capabilityApplicationMapping = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
