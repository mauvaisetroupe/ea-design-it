import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICapability } from '@/shared/model/capability.model';
import CapabilityService from './capability.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CapabilityDetails extends Vue {
  @Inject('capabilityService') private capabilityService: () => CapabilityService;
  @Inject('alertService') private alertService: () => AlertService;

  public path = [];

  public capability: ICapability = {};
  public capabilitiesPlantUMLImage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.capabilityId) {
        vm.retrieveCapability(to.params.capabilityId);
      }
    });
  }

  public retrieveCapability(capabilityId) {
    this.capabilityService()
      .find(capabilityId)
      .then(res => {
        this.capability = res;
        let tmp = this.capability;
        this.path = [];
        tmp = tmp.parent;
        while (tmp) {
          this.path.push(tmp);
          tmp = tmp.parent;
        }
        this.path.reverse();
        console.log(this.path);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
