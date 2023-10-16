import { Component, Vue, Inject, Watch } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';
import CapabilityTreeItemComponent from '@/entities/capability/component/capability-tree-item.vue';

const CapabilityProps = Vue.extend({
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
  },
});

@Component({
  components: {
    CapabilityTreeItemComponent,
  },
})
export default class CapabilityTreeComponent extends CapabilityProps {
 
}
