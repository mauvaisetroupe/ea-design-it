import type { IObjectWithParent } from '@/shared/model/object-with-parent.model';
import { defineComponent, ref, type PropType, computed } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BusinessAndDataObjectFullpath',
  props: {
    objectWithParent: {
      type: Object as PropType<IObjectWithParent>,
      required: true,
    },
    routerView: {
      type: String,
    },
    routerParamName: {
      type: String,
    },
    generalization: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, context) {
    const allObjects: IObjectWithParent[] = computed(() => {
      const allParents: IObjectWithParent[] = [];
      let currentObj: IObjectWithParent | null = props.objectWithParent;
      while (currentObj) {
        console.log(currentObj.name);
        allParents.unshift(currentObj);
        currentObj = currentObj.parent;
      }
      return allParents;
    });

    const to = { name: props.routerView, params: { businessObjectId: props.objectWithParent.id } };
    return {
      allObjects,
    };
  },
});
