import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import FunctionalFlowStepService from './functional-flow-step.service';
import { type IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowStep',
  setup() {
    const functionalFlowStepService = inject('functionalFlowStepService', () => new FunctionalFlowStepService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const functionalFlowSteps: Ref<IFunctionalFlowStep[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveFunctionalFlowSteps = async () => {
      isFetching.value = true;
      try {
        const res = await functionalFlowStepService().retrieve();
        functionalFlowSteps.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveFunctionalFlowSteps();
    };

    onMounted(async () => {
      await retrieveFunctionalFlowSteps();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IFunctionalFlowStep) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeFunctionalFlowStep = async () => {
      try {
        await functionalFlowStepService().delete(removeId.value);
        const message = 'A FunctionalFlowStep is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveFunctionalFlowSteps();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      functionalFlowSteps,
      handleSyncList,
      isFetching,
      retrieveFunctionalFlowSteps,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeFunctionalFlowStep,
    };
  },
});
