import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import LandscapeViewService from './landscape-view.service';
import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LandscapeView',
  setup() {
    const dataUtils = useDataUtils();
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const landscapeViews: Ref<ILandscapeView[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveLandscapeViews = async () => {
      isFetching.value = true;
      try {
        const res = await landscapeViewService().retrieve();
        landscapeViews.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
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

    const removeId: Ref<number> = ref(null);
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
        await landscapeViewService().delete(removeId.value);
        const message = 'A LandscapeView is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveLandscapeViews();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
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
      ...dataUtils,
    };
  },
});
