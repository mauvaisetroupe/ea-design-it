import { Component, Vue, Inject } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';

const CapabilityiItemProps = Vue.extend({
  props: {
    capability: {
      type: Object as PropType<ICapability>,
    },
    showApplications: Boolean,
    showInheritedApplications: Boolean,
    childStyle: String,
    nbLevel: Number,
  },
});

@Component
export default class CapabilityItemComponent extends CapabilityiItemProps {}
