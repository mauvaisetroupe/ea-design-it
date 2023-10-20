import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import TechnologyService from './technology.service';
import { type ITechnology } from '@/shared/model/technology.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Technology',
  setup() {
    const technologyService = inject('technologyService', () => new TechnologyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const technologies: Ref<ITechnology[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveTechnologys = async () => {
      isFetching.value = true;
      try {
        const res = await technologyService().retrieve();
        technologies.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveTechnologys();
    };

    onMounted(async () => {
      await retrieveTechnologys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ITechnology) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeTechnology = async () => {
      try {
        await technologyService().delete(removeId.value);
        const message = 'A Technology is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveTechnologys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      technologies,
      handleSyncList,
      isFetching,
      retrieveTechnologys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeTechnology,
    };
  },
});
