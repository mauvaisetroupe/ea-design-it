import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ExternalSystemService from './external-system.service';
import { type IExternalSystem } from '@/shared/model/external-system.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalSystemDetails',
  setup() {
    const externalSystemService = inject('externalSystemService', () => new ExternalSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const externalSystem: Ref<IExternalSystem> = ref({});

    const retrieveExternalSystem = async externalSystemId => {
      try {
        const res = await externalSystemService().find(externalSystemId);
        externalSystem.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.externalSystemId) {
      retrieveExternalSystem(route.params.externalSystemId);
    }

    return {
      alertService,
      externalSystem,

      previousState,
    };
  },
});
