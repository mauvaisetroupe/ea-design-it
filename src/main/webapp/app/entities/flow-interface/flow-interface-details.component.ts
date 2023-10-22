import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FlowInterfaceService from './flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterfaceDetails',
  setup() {
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const flowInterface: Ref<IFlowInterface> = ref({});

    const retrieveFlowInterface = async flowInterfaceId => {
      try {
        const res = await flowInterfaceService().find(flowInterfaceId);
        flowInterface.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.flowInterfaceId) {
      retrieveFlowInterface(route.params.flowInterfaceId);
    }

    return {
      alertService,
      flowInterface,

      previousState,
    };
  },
});
