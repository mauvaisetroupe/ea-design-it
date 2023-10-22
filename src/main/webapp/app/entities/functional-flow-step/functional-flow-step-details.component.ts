import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FunctionalFlowStepService from './functional-flow-step.service';
import { type IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowStepDetails',
  setup() {
    const functionalFlowStepService = inject('functionalFlowStepService', () => new FunctionalFlowStepService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const functionalFlowStep: Ref<IFunctionalFlowStep> = ref({});

    const retrieveFunctionalFlowStep = async functionalFlowStepId => {
      try {
        const res = await functionalFlowStepService().find(functionalFlowStepId);
        functionalFlowStep.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.functionalFlowStepId) {
      retrieveFunctionalFlowStep(route.params.functionalFlowStepId);
    }

    return {
      alertService,
      functionalFlowStep,

      previousState,
    };
  },
});
