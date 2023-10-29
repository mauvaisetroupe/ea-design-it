import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CapabilityService from './capability.service';
import { type ICapability } from '@/shared/model/capability.model';
import { useAlertService } from '@/shared/alert/alert.service';
import CapabilityComponent from '@/entities/capability/component/capability.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityDetails',
  components: {
    CapabilityComponent,
  },
  setup() {
    const capabilityService = inject('capabilityService', () => new CapabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const capability: Ref<ICapability> = ref({});
    const flattenCapabilities: Ref<string[]> = ref([]);
    const filter: Ref<string> = ref('');
    const isFetching: Ref<boolean> = ref(false);

    const filteredCapabilities = computed(() => {
      if (filter.value) {
        return flattenCapabilities.value.filter(s => s.toLowerCase().includes(filter.value.toLowerCase()));
      } else {
        return flattenCapabilities.value;
      }
    });

    const retrieveCapability = async (capabilityId: string | null) => {
      isFetching.value = true;
      try {
        if (!capabilityId) {
          const res = await capabilityService().findRoot();
          capability.value = res;
        } else {
          const res = await capabilityService().find(parseInt(capabilityId));
          capability.value = res;
        }
        computeflattenCapabilities(capability.value, flattenCapabilities.value, '');
      } catch (error) {
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    };

    function computeflattenCapabilities(capability: ICapability, fullPathCapabilities: string[], parentPath: string) {
      let fullPath = '';
      if (capability.name !== 'ROOT') {
        let sep = '';
        if (parentPath) {
          sep = ' > ';
        }
        fullPath = parentPath + sep + capability.name;
        fullPathCapabilities.push(fullPath);
      }
      if (capability.subCapabilities) {
        capability.subCapabilities.forEach(cap => computeflattenCapabilities(cap, fullPathCapabilities, fullPath));
      }
    }

    if (route.params?.capabilityId) {
      retrieveCapability(route.params.capabilityId);
    } else {
      retrieveCapability(null);
    }

    return {
      isFetching,
      alertService,
      capability,
      filter,
      filteredCapabilities,
      previousState,
      retrieveCapability,
    };
  },
});
