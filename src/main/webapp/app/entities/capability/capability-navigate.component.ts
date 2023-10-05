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

  public capability: ICapability = {};
  public flattenCapabilities: string[] = [];
  public filter = '';
  public isFetching = true;

  get filteredCapabilities() {
    if (this.filter) {
      return this.flattenCapabilities.filter(s => s.toLowerCase().includes(this.filter.toLowerCase()));
    } else {
      return this.flattenCapabilities;
    }
  }

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.retrieveCapability(to.params.capabilityId);
    });
  }

  public retrieveCapability(capabilityId) {
    console.log('Finding capabilty : ' + capabilityId);
    this.isFetching = true;
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

  public computeflattenCapabilities(capability: ICapability, fullPathCapabilities: string[], parentPath: string) {
    let fullPath = '';
    if (capability.name !== 'ROOT') {
      let sep = '';
      if (parentPath) {
        sep = ' > ';
      }
      fullPath = parentPath + sep + capability.name;
      fullPathCapabilities.push(fullPath);
    }
    if (capability.subCapabilities) {
      capability.subCapabilities.forEach(cap => this.computeflattenCapabilities(cap, fullPathCapabilities, fullPath));
    }
  }

  public previousState() {
    this.$router.go(-1);
  }

  private init(res: ICapability) {
    this.capability = res;
    this.initFlatten().then(flat => {
      this.flattenCapabilities = flat;
    });
    this.isFetching = false;
  }

  private initFlatten(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const flatten = [];
      this.computeflattenCapabilities(this.capability, flatten, '');
      resolve(flatten);
    });
  }
}
