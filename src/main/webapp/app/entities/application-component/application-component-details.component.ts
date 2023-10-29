import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ApplicationComponentService from './application-component.service';
import { type IApplicationComponent } from '@/shared/model/application-component.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationComponentDetails',
  setup() {
    const applicationComponentService = inject('applicationComponentService', () => new ApplicationComponentService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const applicationComponent: Ref<IApplicationComponent> = ref({});

    const retrieveApplicationComponent = async applicationComponentId => {
      try {
        const res = await applicationComponentService().find(applicationComponentId);
        applicationComponent.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.applicationComponentId) {
      retrieveApplicationComponent(route.params.applicationComponentId);
    }

    return {
      alertService,
      applicationComponent,
      accountService,
      previousState,
    };
  },
});
