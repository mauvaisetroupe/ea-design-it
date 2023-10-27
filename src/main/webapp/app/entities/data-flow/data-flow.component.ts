import { computed, defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DataFlowService from './data-flow.service';
import { type IDataFlow } from '@/shared/model/data-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlow',
  setup() {
    const dataFlowService = inject('dataFlowService', () => new DataFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const filteredRows = computed(() => {
      return dataFlows.value.filter(row => {
        const data_id = row.id.toString().toLowerCase();
        const name = row.resourceName ? row.resourceName.toString().toLowerCase() : '';
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const frequency = row.frequency ? row.frequency.toString().toLowerCase() : '';
        const format = row.format ? row.format.name.toString().toLowerCase() : '';
        const flowInterface = row.flowInterface ? row.flowInterface.alias.toString().toLowerCase() : '';
        const protocol =
          row.flowInterface != null && row.flowInterface.protocol != null ? row.flowInterface.protocol.name.toString().toLowerCase() : '';

        const searchTerm = filter.value.toLowerCase();

        return (
          data_id.includes(searchTerm) ||
          name.includes(searchTerm) ||
          description.includes(searchTerm) ||
          frequency.includes(searchTerm) ||
          format.includes(searchTerm) ||
          flowInterface.includes(searchTerm) ||
          protocol.includes(searchTerm)
        );
      });
    });

    const dataFlows: Ref<IDataFlow[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const filter = ref('');

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
      accountService,
      filteredRows,
      filter,
    };
  },
});
