import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ExternalReferenceService from './external-reference.service';
import { type IExternalReference } from '@/shared/model/external-reference.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalReferenceDetails',
  setup() {
    const externalReferenceService = inject('externalReferenceService', () => new ExternalReferenceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const externalReference: Ref<IExternalReference> = ref({});

    const retrieveExternalReference = async externalReferenceId => {
      try {
        const res = await externalReferenceService().find(externalReferenceId);
        externalReference.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.externalReferenceId) {
      retrieveExternalReference(route.params.externalReferenceId);
    }

    return {
      alertService,
      externalReference,

      previousState,
    };
  },
});
