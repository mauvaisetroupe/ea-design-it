import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ProtocolService from './protocol.service';
import { type IProtocol } from '@/shared/model/protocol.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Protocol',
  setup() {
    const protocolService = inject('protocolService', () => new ProtocolService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const protocols: Ref<IProtocol[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveProtocols = async () => {
      isFetching.value = true;
      try {
        const res = await protocolService().retrieve();
        protocols.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveProtocols();
    };

    onMounted(async () => {
      await retrieveProtocols();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IProtocol) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeProtocol = async () => {
      try {
        await protocolService().delete(removeId.value);
        const message = 'A Protocol is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveProtocols();
        closeDialog();
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    return {
      protocols,
      handleSyncList,
      isFetching,
      retrieveProtocols,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeProtocol,
      accountService,
    };
  },
});
