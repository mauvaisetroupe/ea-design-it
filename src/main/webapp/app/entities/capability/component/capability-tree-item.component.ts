import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { type ICapability } from '@/shared/model/capability.model';
import type { PropType } from 'vue';
import CapabilityTreeItemComponent from './capability-tree-item.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityTreeItemComponent',
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
  },
  components: {
    CapabilityTreeItemComponent,
  },
  setup(props, context) {
    const isOpen = ref(false);

    const fullname = computed(() => {
      let fullname = '';
      let sep = '';
      let tmpCapability = props.capability;
      while (tmpCapability) {
        fullname = tmpCapability.name + sep + fullname;
        tmpCapability = tmpCapability.parent;
        sep = ' > ';
      }
      return fullname;
    });

    const isFolder = computed(() => {
      return props.capability.subCapabilities && props.capability.subCapabilities.length;
    });

    function toggle() {
      if (isFolder.value) {
        isOpen.value = !isOpen.value;
      }
    }

    return {};
  },
});
