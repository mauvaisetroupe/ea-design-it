import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ExternalReferenceService from './external-reference.service';
import { type IExternalReference } from '@/shared/model/external-reference.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalReference',
  setup() {
    const externalReferenceService = inject('externalReferenceService', () => new ExternalReferenceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalReferences: Ref<IExternalReference[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveExternalReferences = async () => {
      isFetching.value = true;
      try {
        const res = await externalReferenceService().retrieve();
        externalReferences.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveExternalReferences();
    };

    onMounted(async () => {
      await retrieveExternalReferences();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IExternalReference) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeExternalReference = async () => {
      try {
        await externalReferenceService().delete(removeId.value);
        const message = 'A ExternalReference is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveExternalReferences();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      externalReferences,
      handleSyncList,
      isFetching,
      retrieveExternalReferences,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeExternalReference,
    };
  },
});
