import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import FlowGroupService from './flow-group.service';
import { type IFlowGroup } from '@/shared/model/flow-group.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowGroup',
  setup() {
    const flowGroupService = inject('flowGroupService', () => new FlowGroupService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const flowGroups: Ref<IFlowGroup[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveFlowGroups = async () => {
      isFetching.value = true;
      try {
        const res = await flowGroupService().retrieve();
        flowGroups.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveFlowGroups();
    };

    onMounted(async () => {
      await retrieveFlowGroups();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IFlowGroup) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeFlowGroup = async () => {
      try {
        await flowGroupService().delete(removeId.value);
        const message = 'A FlowGroup is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveFlowGroups();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      flowGroups,
      handleSyncList,
      isFetching,
      retrieveFlowGroups,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeFlowGroup,
    };
  },
});
