import { Component, Vue, Inject } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';
import { IApplication } from '@/shared/model/application.model';

const CapabilityiItemProps = Vue.extend({
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
    showApplications: Boolean,
    showInheritedApplications: Boolean,
    childStyle: String,
    nbLevel: Number,
    nbNode: {
      type: Number,
      default: 6,
    },
  },
});

@Component
export default class CapabilityItemComponent extends CapabilityiItemProps {
  public get percent() {
    if (this.capability.level >= 2) {
      return 100;
    } else {
      var percent: number = Math.max(16, Math.floor(100 / this.nbNode - 1));
      return percent;
    }
  }

  public getApplications(): IApplication[] {
    var applications: IApplication[] = [];
    if (this.capability && this.capability.capabilityApplicationMappings && this.capability.capabilityApplicationMappings.length > 0) {
      applications = this.capability.capabilityApplicationMappings.map(cm => cm.application);
    }
    return applications;
  }

  public getInheritedApplications(): IApplication[] {
    var inheritedApplication: IApplication[] = [];
    this.findInheritedApplication(this.capability, inheritedApplication);
    const ids = inheritedApplication.map(({ id }) => id);
    const filtered = inheritedApplication.filter(({ id }, index) => !ids.includes(id, index + 1));
    return filtered;
  }

  private findInheritedApplication(capability: ICapability, inheritedApplication: IApplication[]) {
    if (capability.subCapabilities) {
      capability.subCapabilities.forEach(c => {
        if (c.capabilityApplicationMappings) {
          c.capabilityApplicationMappings.forEach(cm => inheritedApplication.push(cm.application));
        }
        this.findInheritedApplication(c, inheritedApplication);
      });
    }
  }
}
