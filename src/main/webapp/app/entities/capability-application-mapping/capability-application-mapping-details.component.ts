import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CapabilityApplicationMappingService from './capability-application-mapping.service';
import { type ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityApplicationMappingDetails',
  setup() {
    const capabilityApplicationMappingService = inject(
      'capabilityApplicationMappingService',
      () => new CapabilityApplicationMappingService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const capabilityApplicationMapping: Ref<ICapabilityApplicationMapping> = ref({});

    const retrieveCapabilityApplicationMapping = async capabilityApplicationMappingId => {
      try {
        const res = await capabilityApplicationMappingService().find(capabilityApplicationMappingId);
        capabilityApplicationMapping.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.capabilityApplicationMappingId) {
      retrieveCapabilityApplicationMapping(route.params.capabilityApplicationMappingId);
    }

    return {
      alertService,
      capabilityApplicationMapping,

      previousState,
    };
  },
});
