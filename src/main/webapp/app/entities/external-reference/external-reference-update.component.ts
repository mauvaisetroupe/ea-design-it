import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ExternalReferenceService from './external-reference.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExternalSystemService from '@/entities/external-system/external-system.service';
import { type IExternalSystem } from '@/shared/model/external-system.model';
import { type IExternalReference, ExternalReference } from '@/shared/model/external-reference.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalReferenceUpdate',
  setup() {
    const externalReferenceService = inject('externalReferenceService', () => new ExternalReferenceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalReference: Ref<IExternalReference> = ref(new ExternalReference());

    const externalSystemService = inject('externalSystemService', () => new ExternalSystemService());

    const externalSystems: Ref<IExternalSystem[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      externalSystemService()
        .retrieve()
        .then(res => {
          externalSystems.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      externalID: {},
      externalSystem: {},
      applications: {},
      components: {},
    };
    const v$ = useVuelidate(validationRules, externalReference as any);
    v$.value.$validate();

    return {
      externalReferenceService,
      alertService,
      externalReference,
      previousState,
      isSaving,
      currentLanguage,
      externalSystems,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.externalReference.id) {
        this.externalReferenceService()
          .update(this.externalReference)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A ExternalReference is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.externalReferenceService()
          .create(this.externalReference)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A ExternalReference is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
