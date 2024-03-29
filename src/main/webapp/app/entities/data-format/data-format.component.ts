import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import DataFormatService from './data-format.service';
import { type IDataFormat } from '@/shared/model/data-format.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFormat',
  setup() {
    const dataFormatService = inject('dataFormatService', () => new DataFormatService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const dataFormats: Ref<IDataFormat[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDataFormats = async () => {
      isFetching.value = true;
      try {
        const res = await dataFormatService().retrieve();
        dataFormats.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDataFormats();
    };

    onMounted(async () => {
      await retrieveDataFormats();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDataFormat) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDataFormat = async () => {
      try {
        await dataFormatService().delete(removeId.value);
        const message = 'A DataFormat is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDataFormats();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      dataFormats,
      handleSyncList,
      isFetching,
      retrieveDataFormats,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDataFormat,
      accountService,
    };
  },
});
