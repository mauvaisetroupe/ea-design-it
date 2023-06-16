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
  public capabilitiesPlantUMLImage = '';

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
  }
}
