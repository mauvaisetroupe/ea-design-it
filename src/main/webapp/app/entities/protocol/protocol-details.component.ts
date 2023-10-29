import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ProtocolService from './protocol.service';
import { type IProtocol } from '@/shared/model/protocol.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProtocolDetails',
  setup() {
    const protocolService = inject('protocolService', () => new ProtocolService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const protocol: Ref<IProtocol> = ref({});

    const retrieveProtocol = async protocolId => {
      try {
        const res = await protocolService().find(protocolId);
        protocol.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.protocolId) {
      retrieveProtocol(route.params.protocolId);
    }

    return {
      alertService,
      protocol,
      accountService,
      previousState,
    };
  },
});
