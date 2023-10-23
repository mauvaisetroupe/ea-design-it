import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DataFlowItemService from './data-flow-item.service';
import { type IDataFlowItem } from '@/shared/model/data-flow-item.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowItem',
  setup() {
    const dataFlowItemService = inject('dataFlowItemService', () => new DataFlowItemService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const dataFlowItems: Ref<IDataFlowItem[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDataFlowItems = async () => {
      isFetching.value = true;
      try {
        const res = await dataFlowItemService().retrieve();
        dataFlowItems.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDataFlowItems();
    };

    onMounted(async () => {
      await retrieveDataFlowItems();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDataFlowItem) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDataFlowItem = async () => {
      try {
        await dataFlowItemService().delete(removeId.value);
        const message = 'A DataFlowItem is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDataFlowItems();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      dataFlowItems,
      handleSyncList,
      isFetching,
      retrieveDataFlowItems,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDataFlowItem,
      accountService,
    };
  },
});
