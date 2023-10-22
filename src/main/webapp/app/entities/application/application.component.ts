import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ApplicationService from './application.service';
import { type IApplication } from '@/shared/model/application.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Application',
  setup() {
    const applicationService = inject('applicationService', () => new ApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applications: Ref<IApplication[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveApplications = async () => {
      isFetching.value = true;
      try {
        const res = await applicationService().retrieve();
        applications.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApplications();
    };

    onMounted(async () => {
      await retrieveApplications();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IApplication) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeApplication = async () => {
      try {
        await applicationService().delete(removeId.value);
        const message = 'A Application is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveApplications();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      applications,
      handleSyncList,
      isFetching,
      retrieveApplications,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeApplication,
    };
  },
});
