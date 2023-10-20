import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FunctionalFlowService from './functional-flow.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import { type IFunctionalFlow, FunctionalFlow } from '@/shared/model/functional-flow.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowUpdate',
  setup() {
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const functionalFlow: Ref<IFunctionalFlow> = ref(new FunctionalFlow());

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFunctionalFlow = async functionalFlowId => {
      try {
        const res = await functionalFlowService().find(functionalFlowId);
        functionalFlow.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.functionalFlowId) {
      retrieveFunctionalFlow(route.params.functionalFlowId);
    }

    const initRelationships = () => {
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      alias: {},
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      comment: {
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      status: {},
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL2: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      steps: {},
      owner: {},
      landscapes: {},
      dataFlows: {},
    };
    const v$ = useVuelidate(validationRules, functionalFlow as any);
    v$.value.$validate();

    return {
      functionalFlowService,
      alertService,
      functionalFlow,
      previousState,
      isSaving,
      currentLanguage,
      owners,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.functionalFlow.id) {
        this.functionalFlowService()
          .update(this.functionalFlow)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A FunctionalFlow is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.functionalFlowService()
          .create(this.functionalFlow)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A FunctionalFlow is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
