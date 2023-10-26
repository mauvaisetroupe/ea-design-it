import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ApplicationComponentService from './application-component.service';
import { type IApplicationComponent } from '@/shared/model/application-component.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationComponent',
  setup() {
    const applicationComponentService = inject('applicationComponentService', () => new ApplicationComponentService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const applicationComponents: Ref<IApplicationComponent[]> = ref([]);

    const isFetching = ref(false);

    const fields = [
      // { key: 'id', sortable: false },
      { key: 'alias', sortable: true },
      { key: 'name', sortable: true },
      { key: 'application', sortable: false },
      { key: 'description', sortable: false, formatter: 'formatLongText' },
      { key: 'applicationType', sortable: false },
      { key: 'softwareType', sortable: false },
      { key: 'categories', sortable: false },
      { key: 'technologies', sortable: false },
    ];

    const currentPage = ref(1);
    const perPage = ref(10);
    const filter = ref('');

    const clear = () => {};

    const retrieveApplicationComponents = async () => {
      isFetching.value = true;
      try {
        const res = await applicationComponentService().retrieve();
        applicationComponents.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApplicationComponents();
    };

    onMounted(async () => {
      await retrieveApplicationComponents();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IApplicationComponent) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeApplicationComponent = async () => {
      try {
        await applicationComponentService().delete(removeId.value);
        const message = 'A ApplicationComponent is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveApplicationComponents();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      applicationComponents,
      handleSyncList,
      isFetching,
      retrieveApplicationComponents,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeApplicationComponent,
      accountService,
      currentPage,
      perPage,
      filter,
      fields,
    };
  },
});
