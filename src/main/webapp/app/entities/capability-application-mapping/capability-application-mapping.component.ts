import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import CapabilityApplicationMappingService from './capability-application-mapping.service';
import { type ICapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityApplicationMapping',
  setup() {
    const capabilityApplicationMappingService = inject(
      'capabilityApplicationMappingService',
      () => new CapabilityApplicationMappingService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const capabilityApplicationMappings: Ref<ICapabilityApplicationMapping[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCapabilityApplicationMappings = async () => {
      isFetching.value = true;
      try {
        const res = await capabilityApplicationMappingService().retrieve();
        capabilityApplicationMappings.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCapabilityApplicationMappings();
    };

    onMounted(async () => {
      await retrieveCapabilityApplicationMappings();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICapabilityApplicationMapping) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCapabilityApplicationMapping = async () => {
      try {
        await capabilityApplicationMappingService().delete(removeId.value);
        const message = 'A CapabilityApplicationMapping is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCapabilityApplicationMappings();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      capabilityApplicationMappings,
      handleSyncList,
      isFetching,
      retrieveCapabilityApplicationMappings,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCapabilityApplicationMapping,
    };
  },
});
