import { Component, Vue, Inject, Watch } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';
import CapabilityComponentItem from '@/entities/capability/component/capability-item.vue';

const CapabilityProps = Vue.extend({
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
});

@Component({
  components: {
    CapabilityComponentItem,
  },
})
export default class CapabilityComponent extends CapabilityProps {
  public showApplications: boolean = this.defaultShowApplications;
  public path = [];
  public mapNbCapabilitiesByLevel: Map<number, number> = new Map();
  public mapNbCapabilitiesByLevelAndSubLevel: Map<number, number> = new Map();
  public get nbLevel() {
    return parseInt(this.nbLevelAsString);
  }
  public nbLevelAsString = ''; // input type range send string

  public retrieveCapability(capId: number) {
    this.$emit('retrieveCapability', capId);
  }

  mounted() {
    this.capabilityUpdated();
  }

  @Watch('capability')
  public capabilityUpdated() {
    console.log(this.capability.name);
    console.log(this.defaultNbLevel);
    if (this.defaultNbLevel) {
      this.nbLevelAsString = this.defaultNbLevel.toString();
    } else {
      this.nbLevelAsString = Math.min(3, this.capability.level + 3).toString();
    }

    // PATH for breadcrumb
    let tmp = this.capability;
    this.path = [];
    tmp = tmp.parent;
    while (tmp) {
      this.path.push(tmp);
      tmp = tmp.parent;
    }
    this.path.reverse();

    // Calculate nb max capabilities by level
    this.mapNbCapabilitiesByLevel = new Map();
    this.mapNbCapabilitiesByLevel.set(this.capability.level, 1);
    this.mapNbCapabilitiesByLevelAndSubLevel = new Map();

    this.calculateMaxByLevel(this.capability);
  }

  public calculateMaxByLevel(capa: ICapability) {
    const key = capa.level + 1;
    if (capa.subCapabilities) {
      let max = 0;
      if (this.mapNbCapabilitiesByLevel.get(key)) {
        max = this.mapNbCapabilitiesByLevel.get(key);
      }
      max = Math.max(max, capa.subCapabilities.length);
      this.mapNbCapabilitiesByLevel.set(key, max);
      capa.subCapabilities.forEach(c => this.calculateMaxByLevel(c));
    }
  }
}
