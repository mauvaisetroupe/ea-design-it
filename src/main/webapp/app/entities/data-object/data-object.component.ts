import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DataObjectService from './data-object.service';
import { type IDataObject } from '@/shared/model/data-object.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import BusinessAndDataObjectFullpath from '@/eadesignit/components/business-data-object-fullpath.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataObject',
  components: {
    BusinessAndDataObjectFullpath,
  },
  setup() {
    const dataObjectService = inject('dataObjectService', () => new DataObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const dataObjects: Ref<IDataObject[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDataObjects = async () => {
      isFetching.value = true;
      try {
        const res = await dataObjectService().retrieve(true);
        dataObjects.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDataObjects();
    };

    onMounted(async () => {
      await retrieveDataObjects();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDataObject) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDataObject = async () => {
      try {
        await dataObjectService().delete(removeId.value);
        const message = 'A DataObject is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDataObjects();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      dataObjects,
      handleSyncList,
      isFetching,
      retrieveDataObjects,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDataObject,
      accountService,
    };
  },
});
