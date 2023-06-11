import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICapability } from '@/shared/model/capability.model';
import CapabilityService from './capability.service';
import AlertService from '@/shared/alert/alert.service';

import CapabilityComponent from '@/entities/capability/component/capability.vue';
import { IApplication } from '@/shared/model/application.model';

@Component({
  components: {
    CapabilityComponent,
  },
})
export default class CapabilityDetails extends Vue {
  @Inject('capabilityService') private capabilityService: () => CapabilityService;
  @Inject('alertService') private alertService: () => AlertService;

  public path = [];

  public capability: ICapability = {};
  public capabilitiesPlantUMLImage = '';
  public maxLevel = 0;
  public nbLevel = 0;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.retrieveCapability(to.params.capabilityId);
    });
  }

  public retrieveCapability(capabilityId) {
    console.log('Finding capabilty : ' + capabilityId);
    if (!capabilityId) {
      this.capabilityService()
        .findRoot()
        .then(res => {
          this.init(res);
        })
        .catch(error => {
          console.log('Something wrong when finding root');
          console.log(error);
        });
    } else {
      this.capabilityService()
        .find(capabilityId)
        .then(res => {
          this.init(res);
        })
        .catch(error => {
          console.log('Something wrong when capability ' + capabilityId);
          console.log(error);
        });
    }
  }

  public previousState() {
    this.$router.go(-1);
  }

  private init(res: ICapability) {
    this.capability = res;

    // inherited
    console.log(this.capability.name + ' ' + this.capability.level);
    this.addInheritedApplications(this.capability);
    this.nbLevel = Math.min(3, this.capability.level + 3);

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

  private addInheritedApplications(capability: ICapability) {
    if (capability.subCapabilities) {
      capability.subCapabilities.forEach(c => this.addInheritedApplications(c));
    }
    let inheritedApplication: IApplication[] = [];
    this.findInheritedApplication(capability, inheritedApplication);
    const ids = inheritedApplication.map(({ id }) => id);
    const filtered = inheritedApplication.filter(({ id }, index) => !ids.includes(id, index + 1));
    capability.inheritedApplications = filtered;
  }

  private findInheritedApplication(capability: ICapability, inheritedApplication: IApplication[]) {
    if (capability.subCapabilities) {
      capability.subCapabilities.forEach(c => {
        if (c.applications) {
          c.applications.forEach(a => inheritedApplication.push(a));
        }
        this.findInheritedApplication(c, inheritedApplication);
      });
    }
  }

  public calulateMax(capability: ICapability, arg1: number): number {
    // console.log('IN : ' + arg1);
    var max = 0;
    if (capability && capability.subCapabilities) {
      for (const cap of capability.subCapabilities) {
        var tmp = this.calulateMax(cap, arg1 + 1);
        if (tmp > max) {
          max = tmp;
        }
      }
    }
    // console.log('OUT : ' + max);
    return max + 1;
  }
}
