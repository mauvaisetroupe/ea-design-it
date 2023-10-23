import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CapabilityService from './capability.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ICapability, type Capability } from '@/shared/model/capability.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityUpdate',
  setup() {
    const capabilityService = inject('capabilityService', () => new CapabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const capability: Ref<ICapability> = ref(new Capability());

    const capabilities: Ref<ICapability[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCapability = async capabilityId => {
      try {
        const res = await capabilityService().find(capabilityId);
        capability.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.capabilityId) {
      retrieveCapability(route.params.capabilityId);
    }

    const initRelationships = () => {
      capabilityService()
        .retrieve()
        .then(res => {
          capabilities.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      comment: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      level: {},
      subCapabilities: {},
      parent: {},
      capabilityApplicationMappings: {},
    };
    const v$ = useVuelidate(validationRules, capability as any);
    v$.value.$validate();

    return {
      capabilityService,
      alertService,
      capability,
      previousState,
      isSaving,
      currentLanguage,
      capabilities,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.capability.id) {
        this.capabilityService()
          .update(this.capability)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Capability is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.capabilityService()
          .create(this.capability)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Capability is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
