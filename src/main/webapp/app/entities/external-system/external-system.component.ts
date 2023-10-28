import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ExternalSystemService from './external-system.service';
import { type IExternalSystem } from '@/shared/model/external-system.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalSystem',
  setup() {
    const externalSystemService = inject('externalSystemService', () => new ExternalSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalSystems: Ref<IExternalSystem[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveExternalSystems = async () => {
      isFetching.value = true;
      try {
        const res = await externalSystemService().retrieve();
        externalSystems.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveExternalSystems();
    };

    onMounted(async () => {
      await retrieveExternalSystems();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IExternalSystem) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeExternalSystem = async () => {
      try {
        await externalSystemService().delete(removeId.value);
        const message = 'A ExternalSystem is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveExternalSystems();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      externalSystems,
      handleSyncList,
      isFetching,
      retrieveExternalSystems,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeExternalSystem,
    };
  },
});
