import { Component, Vue, Inject } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';

const CapabilityProps = Vue.extend({
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
    path: {
      type: Array as PropType<ICapability[]>,
    },
  },
});

@Component
export default class CapabilityComponent extends CapabilityProps {
  public showApplication: boolean = false;
}
