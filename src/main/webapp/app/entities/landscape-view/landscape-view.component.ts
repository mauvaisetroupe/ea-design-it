import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import LandscapeViewService from './landscape-view.service';
import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LandscapeView',
  setup() {
    const dataUtils = useDataUtils();
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const landscapeViews: Ref<ILandscapeView[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const deleteFunctionalFlows = ref(true);
    const deleteInterfaces = ref(true);
    const deleteDatas = ref(true);
    const deleteCapabilityMappings = ref(false);
    const deleteDataObjects = ref(false);

    function deleteCoherence() {
      if (!deleteFunctionalFlows.value) {
        deleteInterfaces.value = false;
        deleteDatas.value = false;
      }
      if (!deleteInterfaces.value) {
        deleteDatas.value = false;
      }
    }

    const retrieveLandscapeViews = async () => {
      isFetching.value = true;
      try {
        const res = await landscapeViewService().retrieve();
        landscapeViews.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveLandscapeViews();
    };

    onMounted(async () => {
      await retrieveLandscapeViews();
    });

    const removeId: Ref<number> = ref(-1);
    const removeEntity = ref<any>(null);

    const prepareRemove = (instance: ILandscapeView) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeLandscapeView = async () => {
      try {
        await landscapeViewService().delete(
          removeId.value,
          deleteFunctionalFlows.value,
          deleteInterfaces.value,
          deleteDatas.value,
          deleteCapabilityMappings.value,
          deleteDataObjects.value,
        );
        const message = 'A LandscapeView is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = -1;
        retrieveLandscapeViews();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      landscapeViews,
      handleSyncList,
      isFetching,
      retrieveLandscapeViews,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeLandscapeView,
      accountService,
      ...dataUtils,
      deleteInterfaces,
      deleteDatas,
      deleteCoherence,
      deleteFunctionalFlows,
      deleteCapabilityMappings,
      deleteDataObjects,
    };
  },
});
