import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ExternalSystemService from './external-system.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IExternalSystem, ExternalSystem } from '@/shared/model/external-system.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalSystemUpdate',
  setup() {
    const externalSystemService = inject('externalSystemService', () => new ExternalSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalSystem: Ref<IExternalSystem> = ref(new ExternalSystem());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveExternalSystem = async externalSystemId => {
      try {
        const res = await externalSystemService().find(externalSystemId);
        externalSystem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.externalSystemId) {
      retrieveExternalSystem(route.params.externalSystemId);
    }

    const validations = useValidation();
    const validationRules = {
      externalSystemID: {},
    };
    const v$ = useVuelidate(validationRules, externalSystem as any);
    v$.value.$validate();

    return {
      externalSystemService,
      alertService,
      externalSystem,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.externalSystem.id) {
        this.externalSystemService()
          .update(this.externalSystem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A ExternalSystem is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.externalSystemService()
          .create(this.externalSystem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A ExternalSystem is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
