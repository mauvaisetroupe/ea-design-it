import { defineComponent, inject, ref, type PropType, type Ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { Capability } from '@/shared/model/capability.model';
import { type ICapability } from '@/shared/model/capability.model';
import CapabilityComponentItem from '@/entities/capability/component/capability-item.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityComponent',
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
    showPath: {
      type: Boolean,
      default: true,
    },
    showSliders: {
      type: Boolean,
      default: true,
    },
    defaultShowApplications: {
      type: Boolean,
      default: true,
    },
    defaultNbLevel: {
      type: Number,
    },
  },
  components: {
    CapabilityComponentItem,
  },
  emits: ['retrieveCapability'],
  setup(props, context) {
    //const showApplications: Boolean =props.defaultShowApplications
    const showApplications = ref(false);
    const path = ref([]);
    const mapNbCapabilitiesByLevel: Map<number, number> = new Map();
    const mapNbCapabilitiesByLevelAndSubLevel: Map<number, number> = new Map();
    let nbLevelAsString = ''; // input type range send string

    function retrieveCapability(capId: number) {
      context.emit('retrieveCapability', capId);
    }

    const nbLevel = computed(() => {
      return parseInt(nbLevelAsString);
    });

    onMounted(() => {
      capabilityUpdated();
    });

    //   @Watch('capability')
    function capabilityUpdated() {
      console.log(props.capability?.name);
      console.log(props.defaultNbLevel);
      if (props.defaultNbLevel) {
        nbLevelAsString = props.defaultNbLevel.toString();
      } else {
        nbLevelAsString = Math.min(3, props.capability.level + 3).toString();
      }

      // PATH for breadcrumb
      let tmp = props.capability;
      path.value = [];
      tmp = tmp.parent;
      while (tmp) {
        path.value.push(tmp);
        tmp = tmp.parent;
      }
      path.value.reverse();

      // Calculate nb max capabilities by level
      mapNbCapabilitiesByLevel.value = new Map();
      mapNbCapabilitiesByLevel.value.set(props.capability.level, 1);
      mapNbCapabilitiesByLevelAndSubLevel.value = new Map();

      calculateMaxByLevel(props.capability);
    }

    function calculateMaxByLevel(capa: ICapability) {
      const key = capa.level + 1;
      if (capa.subCapabilities) {
        let max = 0;
        if (mapNbCapabilitiesByLevel.value.get(key)) {
          max = mapNbCapabilitiesByLevel.value.get(key);
        }
        max = Math.max(max, capa.subCapabilities.length);
        mapNbCapabilitiesByLevel.value.set(key, max);
        capa.subCapabilities.forEach(c => calculateMaxByLevel(c));
      }
    }

    return {
      showApplications,
      mapNbCapabilitiesByLevel,
      nbLevel,
      retrieveCapability,
    };
  },
});
