import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import FlowInterfaceService from './flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterface',
  setup() {
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const flowInterfaces: Ref<IFlowInterface[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveFlowInterfaces = async () => {
      isFetching.value = true;
      try {
        const res = await flowInterfaceService().retrieve();
        flowInterfaces.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveFlowInterfaces();
    };

    onMounted(async () => {
      await retrieveFlowInterfaces();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IFlowInterface) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeFlowInterface = async () => {
      try {
        await flowInterfaceService().delete(removeId.value);
        const message = 'A FlowInterface is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveFlowInterfaces();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      flowInterfaces,
      handleSyncList,
      isFetching,
      retrieveFlowInterfaces,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeFlowInterface,
    };
  },
});
