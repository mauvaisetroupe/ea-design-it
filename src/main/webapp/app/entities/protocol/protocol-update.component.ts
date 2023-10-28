import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ProtocolService from './protocol.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IProtocol, Protocol } from '@/shared/model/protocol.model';
import { ProtocolType } from '@/shared/model/enumerations/protocol-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProtocolUpdate',
  setup() {
    const protocolService = inject('protocolService', () => new ProtocolService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const protocol: Ref<IProtocol> = ref(new Protocol());
    const protocolTypeValues: Ref<string[]> = ref(Object.keys(ProtocolType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      type: {
        required: validations.required('This field is required.'),
      },
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      scope: {},
    };
    const v$ = useVuelidate(validationRules, protocol as any);
    v$.value.$validate();

    return {
      protocolService,
      alertService,
      protocol,
      previousState,
      protocolTypeValues,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.protocol.id) {
        this.protocolService()
          .update(this.protocol)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Protocol is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      } else {
        this.protocolService()
          .create(this.protocol)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Protocol is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      }
    },
  },
});
