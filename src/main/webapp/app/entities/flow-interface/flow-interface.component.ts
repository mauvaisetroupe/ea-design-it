import { computed, defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import FlowInterfaceService from './flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import { useStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterface',
  setup() {
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');
    const store = useStore();

    const perPage = ref(10);
    const currentPage = ref(1);

    const filteredRows = computed(() => {
      return flowInterfaces.value.filter(row => {
        return (
          (!filterAlias.value || row.alias?.toLowerCase().includes(filterAlias.value?.toLowerCase())) &&
          (!filterSource.value ||
            (row.source?.name?.toLowerCase() + row.sourceComponent?.name?.toLowerCase()).includes(filterSource.value?.toLowerCase())) &&
          (!filterTarget.value ||
            (row.target?.name?.toLowerCase() + row.targetComponent?.name?.toLowerCase()).includes(filterTarget.value?.toLowerCase())) &&
          (!filterProtocol.value || row.protocol?.name?.toLowerCase().includes(filterProtocol.value?.toLowerCase()))
        );
      });
    });

    const removeId: Ref<number> = ref(-1);
    const removeEntity = ref<any>(null);

    const flowInterfaces: Ref<IFlowInterface[]> = ref([]);

    const isFetching = ref(false);

    const filterAlias = ref('');
    const filterSource = ref('');
    const filterTarget = ref('');
    const filterProtocol = ref('');

    onMounted(async () => {
      await retrieveAllFlowInterfaces();
    });

    function clear(): void {
      retrieveAllFlowInterfaces();
    }

    const retrieveAllFlowInterfaces = async () => {
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
      retrieveAllFlowInterfaces();
    };

    const prepareRemove = (instance: IFlowInterface) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };

    const removeFlowInterface = async () => {
      try {
        await flowInterfaceService().delete(removeId.value);
        const message = 'A FlowInterface is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = -1;
        retrieveAllFlowInterfaces();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const closeDialog = () => {
      removeEntity.value.hide();
    };

    function isOwner(flowInterface: IFlowInterface): boolean {
      const username = store.account?.login ?? '';
      if (accountService.writeAuthorities) {
        return true;
      }
      if (flowInterface.owner && flowInterface.owner.users) {
        for (const user of flowInterface.owner.users) {
          if (user.login === username) {
            return true;
          }
        }
      }
      return false;
    }

    return {
      flowInterfaces,
      handleSyncList,
      isFetching,
      retrieveAllFlowInterfaces,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeFlowInterface,
      accountService,
      filteredRows,
      perPage,
      filterAlias,
      filterSource,
      filterTarget,
      filterProtocol,
      currentPage,
      isOwner,
    };
  },
});
