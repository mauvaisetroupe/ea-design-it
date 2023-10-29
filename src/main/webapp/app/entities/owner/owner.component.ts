import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import OwnerService from './owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Owner',
  setup() {
    const ownerService = inject('ownerService', () => new OwnerService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const owners: Ref<IOwner[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveOwners = async () => {
      isFetching.value = true;
      try {
        const res = await ownerService().retrieve();
        owners.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveOwners();
    };

    onMounted(async () => {
      await retrieveOwners();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IOwner) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeOwner = async () => {
      try {
        await ownerService().delete(removeId.value);
        const message = 'A Owner is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveOwners();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      owners,
      handleSyncList,
      isFetching,
      retrieveOwners,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeOwner,
      accountService,
    };
  },
});
