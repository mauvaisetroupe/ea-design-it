import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import TechnologyService from './technology.service';
import { type ITechnology } from '@/shared/model/technology.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TechnologyDetails',
  setup() {
    const technologyService = inject('technologyService', () => new TechnologyService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const technology: Ref<ITechnology> = ref({});

    const retrieveTechnology = async technologyId => {
      try {
        const res = await technologyService().find(technologyId);
        technology.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.technologyId) {
      retrieveTechnology(route.params.technologyId);
    }

    return {
      alertService,
      technology,
      accountService,
      previousState,
    };
  },
});
