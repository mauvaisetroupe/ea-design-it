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
  public maxLevel: number;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.retrieveCapability(to.params.capabilityId);
    });
  }

  public retrieveCapability(capabilityId) {
    if (!capabilityId) {
      this.capabilityService()
        .findRoot()
        .then(res => {
          this.init(res);
        })
        .catch(error => {
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.capabilityService()
        .find(capabilityId)
        .then(res => {
          this.init(res);
        })
        .catch(error => {
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public previousState() {
    this.$router.go(-1);
  }

  private init(res: ICapability) {
    this.capability = res;

    // PATH for breadcrumb
    var tmp = this.capability;
    this.path = [];
    tmp = tmp.parent;
    while (tmp) {
      this.path.push(tmp);
      tmp = tmp.parent;
    }
    this.path.reverse();
    // Maximum level
    this.maxLevel = this.calulateMax(this.capability, 0);
  }

  public calulateMax(capability: ICapability, arg1: number): number {
    console.log('IN : ' + arg1);

    var max = 0;
    for (const cap of capability.subCapabilities) {
      var tmp = this.calulateMax(cap, arg1 + 1);
      if (tmp > max) {
        max = tmp;
      }
    }
    console.log('OUT : ' + max);
    return max + 1;
  }
}
