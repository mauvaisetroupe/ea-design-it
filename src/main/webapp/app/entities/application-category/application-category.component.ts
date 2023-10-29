import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ApplicationCategoryService from './application-category.service';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationCategory',
  setup() {
    const applicationCategoryService = inject('applicationCategoryService', () => new ApplicationCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const applicationCategories: Ref<IApplicationCategory[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveApplicationCategorys = async () => {
      isFetching.value = true;
      try {
        const res = await applicationCategoryService().retrieve();
        applicationCategories.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApplicationCategorys();
    };

    onMounted(async () => {
      await retrieveApplicationCategorys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IApplicationCategory) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeApplicationCategory = async () => {
      try {
        await applicationCategoryService().delete(removeId.value);
        const message = 'A ApplicationCategory is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveApplicationCategorys();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      applicationCategories,
      handleSyncList,
      isFetching,
      retrieveApplicationCategorys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeApplicationCategory,
      accountService,
    };
  },
});
