import { computed, defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import FunctionalFlowService from './functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlow',
  setup() {
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const filteredRows = computed(() => {
      return functionalFlows.value.filter(row => {
        const alias = row.alias ? row.alias.toString().toLowerCase() : '';
        const description = row.description ? row.description.toString().toLowerCase() : '';
        return alias.includes(filterAlias.value?.toLocaleLowerCase()) && description.includes(filterDescription.value?.toLocaleLowerCase());
      });
    });

    const functionalFlows: Ref<IFunctionalFlow[]> = ref([]);

    const isFetching = ref(false);

    const filterAlias = ref('');
    const filterDescription = ref('');

    const deleteInterfaces = ref(true);
    const deleteDatas = ref(true);

    const perPage = ref(10);
    const currentPage = ref(1);

    function deleteCoherence() {
      if (!deleteInterfaces.value) {
        deleteDatas.value = false;
      }
    }

    const clear = () => {};

    const retrieveFunctionalFlows = async () => {
      isFetching.value = true;
      try {
        const res = await functionalFlowService().retrieve();
        functionalFlows.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveFunctionalFlows();
    };

    onMounted(async () => {
      await retrieveFunctionalFlows();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IFunctionalFlow) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeFunctionalFlow = async () => {
      try {
        await functionalFlowService().delete(removeId.value, deleteInterfaces.value, deleteDatas.value);
        const message = 'A FunctionalFlow is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveFunctionalFlows();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      functionalFlows,
      handleSyncList,
      isFetching,
      retrieveFunctionalFlows,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeFunctionalFlow,
      accountService,
      filteredRows,
      perPage,
      currentPage,
      filterDescription,
      filterAlias,
      deleteInterfaces,
      deleteDatas,
      deleteCoherence,
    };
  },
});
