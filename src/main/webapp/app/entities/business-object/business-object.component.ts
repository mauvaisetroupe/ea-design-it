import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import BusinessObjectService from './business-object.service';
import { type IBusinessObject } from '@/shared/model/business-object.model';
import { useAlertService } from '@/shared/alert/alert.service';
import BusinessAndDataObjectFullpath from '@/eadesignit/components/business-data-object-fullpath.vue';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BusinessObject',
  components: {
    BusinessAndDataObjectFullpath,
  },
  setup() {
    const businessObjectService = inject('businessObjectService', () => new BusinessObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const businessObjects: Ref<IBusinessObject[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveBusinessObjects = async () => {
      isFetching.value = true;
      try {
        const res = await businessObjectService().retrieve(true);
        businessObjects.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveBusinessObjects();
    };

    onMounted(async () => {
      await retrieveBusinessObjects();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IBusinessObject) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeBusinessObject = async () => {
      try {
        await businessObjectService().delete(removeId.value);
        const message = 'A BusinessObject is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveBusinessObjects();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      businessObjects,
      handleSyncList,
      isFetching,
      retrieveBusinessObjects,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeBusinessObject,
      accountService,
    };
  },
});
