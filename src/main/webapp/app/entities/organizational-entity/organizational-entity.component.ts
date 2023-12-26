import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import OrganizationalEntityService from './organizational-entity.service';
import { type IOrganizationalEntity } from '@/shared/model/organizational-entity.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OrganizationalEntity',
  setup() {
    const organizationalEntityService = inject('organizationalEntityService', () => new OrganizationalEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const organizationalEntities: Ref<IOrganizationalEntity[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveOrganizationalEntitys = async () => {
      isFetching.value = true;
      try {
        const res = await organizationalEntityService().retrieve();
        organizationalEntities.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveOrganizationalEntitys();
    };

    onMounted(async () => {
      await retrieveOrganizationalEntitys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IOrganizationalEntity) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeOrganizationalEntity = async () => {
      try {
        await organizationalEntityService().delete(removeId.value);
        const message = 'A OrganizationalEntity is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveOrganizationalEntitys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      organizationalEntities,
      handleSyncList,
      isFetching,
      retrieveOrganizationalEntitys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeOrganizationalEntity,
    };
  },
});
