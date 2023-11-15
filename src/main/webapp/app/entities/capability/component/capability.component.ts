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
      required: true,
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
    let mapNbCapabilitiesByLevel: Map<number, number> = new Map();
    let mapNbCapabilitiesByLevelAndSubLevel: Map<number, number> = new Map();
    const nbLevelAsString = ref(''); // input type range send string

    function retrieveCapability(capId: number) {
      context.emit('retrieveCapability', capId);
    }

    const nbLevel = computed(() => {
      return parseInt(nbLevelAsString.value);
    });

    onMounted(() => {
      capabilityUpdated();
    });

    //   @Watch('capability')
    function capabilityUpdated() {
      if (props.defaultNbLevel) {
        nbLevelAsString.value = props.defaultNbLevel.toString();
      } else {
        nbLevelAsString.value = Math.min(3, props.capability.level + 3).toString();
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
      mapNbCapabilitiesByLevel = new Map();
      mapNbCapabilitiesByLevel.set(props.capability.level, 1);
      mapNbCapabilitiesByLevelAndSubLevel = new Map();

      calculateMaxByLevel(props.capability);
    }

    function calculateMaxByLevel(capa: ICapability) {
      const key = capa.level + 1;
      if (capa.subCapabilities) {
        let max = 0;
        if (mapNbCapabilitiesByLevel.get(key)) {
          max = mapNbCapabilitiesByLevel.get(key);
        }
        max = Math.max(max, capa.subCapabilities.length);
        mapNbCapabilitiesByLevel.set(key, max);
        capa.subCapabilities.forEach(c => calculateMaxByLevel(c));
      }
    }

    return {
      showApplications,
      mapNbCapabilitiesByLevel,
      nbLevel,
      nbLevelAsString,
      retrieveCapability,
      path,
    };
  },
});
