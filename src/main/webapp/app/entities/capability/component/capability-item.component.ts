import { defineComponent, inject, ref, type PropType, type Ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';

import { type Capability } from '@/shared/model/capability.model';
import { type ICapability } from '@/shared/model/capability.model';
import { type IApplication } from '@/shared/model/application.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityItemComponent',
  props: {
    capability: {
      type: Object as PropType<ICapability>,
      required: true,
    },
    showApplications: Boolean,
    showInheritedApplications: Boolean,
    childStyle: String,
    nbLevel: {
      type: Number,
      default: 3,
    },
    nbNode: {
      type: Number,
      default: 6,
    },
  },
  setup(props, context) {
    const percent = computed(() => {
      if (props.capability.level && props.capability.level >= 2) {
        return 100;
      } else {
        const percent: number = Math.max(16, Math.floor(100 / props.nbNode - 1));
        return percent;
      }
    });

    function getApplications(): IApplication[] {
      let applications: IApplication[] = [];
      if (props.capability && props.capability.capabilityApplicationMappings && props.capability.capabilityApplicationMappings.length > 0) {
        applications = props.capability.capabilityApplicationMappings.map(cm => cm.application);
      }
      return applications;
    }

    function getInheritedApplications(): IApplication[] {
      const inheritedApplication: IApplication[] = [];
      findInheritedApplication(props.capability, inheritedApplication);
      const ids = inheritedApplication.map(({ id }) => id);
      const filtered = inheritedApplication.filter(({ id }, index) => !ids.includes(id, index + 1));
      return filtered;
    }

    function findInheritedApplication(capability: ICapability, inheritedApplication: IApplication[]) {
      if (capability.subCapabilities) {
        capability.subCapabilities.forEach(c => {
          if (c.capabilityApplicationMappings) {
            c.capabilityApplicationMappings.forEach(cm => inheritedApplication.push(cm.application));
          }
          findInheritedApplication(c, inheritedApplication);
        });
      }
    }

    return {
      percent,
      getInheritedApplications,
      getApplications,
    };
  },
});
