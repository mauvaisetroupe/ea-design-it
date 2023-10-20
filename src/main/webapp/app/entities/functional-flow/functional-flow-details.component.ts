import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FunctionalFlowService from './functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowDetails',
  setup() {
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const functionalFlow: Ref<IFunctionalFlow> = ref({});

    const retrieveFunctionalFlow = async functionalFlowId => {
      try {
        const res = await functionalFlowService().find(functionalFlowId);
        functionalFlow.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.functionalFlowId) {
      retrieveFunctionalFlow(route.params.functionalFlowId);
    }

    return {
      alertService,
      functionalFlow,

      previousState,
    };
  },
});
