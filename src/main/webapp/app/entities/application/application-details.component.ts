import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ApplicationService from './application.service';
import { type IApplication } from '@/shared/model/application.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationDetails',
  setup() {
    const applicationService = inject('applicationService', () => new ApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const application: Ref<IApplication> = ref({});

    const retrieveApplication = async applicationId => {
      try {
        const res = await applicationService().find(applicationId);
        application.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationId) {
      retrieveApplication(route.params.applicationId);
    }

    return {
      alertService,
      application,

      previousState,
    };
  },
});
