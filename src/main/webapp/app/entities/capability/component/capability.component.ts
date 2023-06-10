import { Component, Vue, Inject } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';

const CapabilityProps = Vue.extend({
  props: {
    capability: Capability,
    path: {
      type: Object as PropType<Capability[]>,
    },
  },
});

@Component
export default class CapabilityComponent extends CapabilityProps {}
