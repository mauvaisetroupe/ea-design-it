import { Component, Vue, Inject, Watch } from 'vue-property-decorator';
import { Capability } from '@/shared/model/capability.model';
import { ICapability } from '@/shared/model/capability.model';
import { PropType } from 'vue';

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
export default class CapabilityTreeItemComponent extends CapabilityProps {
 
  public isOpen = false;

  public get fullname() {
    let fullname = '';
    let sep = '';
    let tmpCapability = this.capability;
    while (tmpCapability) {
      fullname = tmpCapability.name + sep + fullname
      tmpCapability = tmpCapability.parent;
      sep = " > ";
    }
    return fullname;
  }


  public get isFolder() {
    return this.capability.subCapabilities && this.capability.subCapabilities.length;
  }  

  public toggle() {
    if (this.isFolder) {
      this.isOpen = !this.isOpen;
    }
  }

}
