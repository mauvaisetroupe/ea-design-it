import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import CapabilityService from './capability.service';
import { type ICapability } from '@/shared/model/capability.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Capability',
  setup() {
    const capabilityService = inject('capabilityService', () => new CapabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const capabilities: Ref<ICapability[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCapabilitys = async () => {
      isFetching.value = true;
      try {
        const res = await capabilityService().retrieve();
        capabilities.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCapabilitys();
    };

    onMounted(async () => {
      await retrieveCapabilitys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICapability) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCapability = async () => {
      try {
        await capabilityService().delete(removeId.value);
        const message = 'A Capability is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCapabilitys();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      capabilities,
      handleSyncList,
      isFetching,
      retrieveCapabilitys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCapability,
    };
  },
});
