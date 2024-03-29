import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import CapabilityService from './capability.service';
import { type ICapability } from '@/shared/model/capability.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityDetails',
  setup() {
    const capabilityService = inject('capabilityService', () => new CapabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const capability: Ref<ICapability> = ref({});

    const retrieveCapability = async capabilityId => {
      try {
        const res = await capabilityService().find(capabilityId);
        capability.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.capabilityId) {
      retrieveCapability(route.params.capabilityId);
    }

    return {
      alertService,
      capability,
      previousState,
      accountService,
      retrieveCapability,
    };
  },
});
