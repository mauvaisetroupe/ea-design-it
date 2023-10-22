import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ICapability } from '@/shared/model/capability.model';
import { type PropType } from 'vue';
import CapabilityTreeItemComponent from '@/entities/capability/component/capability-tree-item.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityTreeComponent',
  components: {
    CapabilityTreeItemComponent,
  },
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
  },
  setup() {
    return {};
  },
});
