import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DataFlowService from './data-flow.service';
import { type IDataFlow } from '@/shared/model/data-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlow',
  setup() {
    const dataFlowService = inject('dataFlowService', () => new DataFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataFlows: Ref<IDataFlow[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDataFlows = async () => {
      isFetching.value = true;
      try {
        const res = await dataFlowService().retrieve();
        dataFlows.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDataFlows();
    };

    onMounted(async () => {
      await retrieveDataFlows();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDataFlow) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDataFlow = async () => {
      try {
        await dataFlowService().delete(removeId.value);
        const message = 'A DataFlow is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDataFlows();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      dataFlows,
      handleSyncList,
      isFetching,
      retrieveDataFlows,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDataFlow,
    };
  },
});
