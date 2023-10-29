import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FunctionalFlowStepService from './functional-flow-step.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import FlowGroupService from '@/entities/flow-group/flow-group.service';
import { type IFlowGroup } from '@/shared/model/flow-group.model';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IFunctionalFlowStep, FunctionalFlowStep } from '@/shared/model/functional-flow-step.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowStepUpdate',
  setup() {
    const functionalFlowStepService = inject('functionalFlowStepService', () => new FunctionalFlowStepService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const functionalFlowStep: Ref<IFunctionalFlowStep> = ref(new FunctionalFlowStep());

    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());

    const flowInterfaces: Ref<IFlowInterface[]> = ref([]);

    const flowGroupService = inject('flowGroupService', () => new FlowGroupService());

    const flowGroups: Ref<IFlowGroup[]> = ref([]);

    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());

    const functionalFlows: Ref<IFunctionalFlow[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFunctionalFlowStep = async functionalFlowStepId => {
      try {
        const res = await functionalFlowStepService().find(functionalFlowStepId);
        functionalFlowStep.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.functionalFlowStepId) {
      retrieveFunctionalFlowStep(route.params.functionalFlowStepId);
    }

    const initRelationships = () => {
      flowInterfaceService()
        .retrieve()
        .then(res => {
          flowInterfaces.value = res.data;
        });
      flowGroupService()
        .retrieve()
        .then(res => {
          flowGroups.value = res.data;
        });
      functionalFlowService()
        .retrieve()
        .then(res => {
          functionalFlows.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      stepOrder: {},
      flowInterface: {
        required: validations.required('This field is required.'),
      },
      group: {},
      flow: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, functionalFlowStep as any);
    v$.value.$validate();

    return {
      functionalFlowStepService,
      alertService,
      functionalFlowStep,
      previousState,
      isSaving,
      currentLanguage,
      flowInterfaces,
      flowGroups,
      functionalFlows,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.functionalFlowStep.id) {
        this.functionalFlowStepService()
          .update(this.functionalFlowStep)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A FunctionalFlowStep is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      } else {
        this.functionalFlowStepService()
          .create(this.functionalFlowStep)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A FunctionalFlowStep is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      }
    },
  },
});
