import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FlowGroupService from './flow-group.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IFlowGroup, FlowGroup } from '@/shared/model/flow-group.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowGroupUpdate',
  setup() {
    const flowGroupService = inject('flowGroupService', () => new FlowGroupService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const flowGroup: Ref<IFlowGroup> = ref(new FlowGroup());

    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());

    const functionalFlows: Ref<IFunctionalFlow[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFlowGroup = async flowGroupId => {
      try {
        const res = await flowGroupService().find(flowGroupId);
        flowGroup.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.flowGroupId) {
      retrieveFlowGroup(route.params.flowGroupId);
    }

    const initRelationships = () => {
      functionalFlowService()
        .retrieve()
        .then(res => {
          functionalFlows.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      title: {
        maxLength: validations.maxLength('This field cannot be longer than 100 characters.', 100),
      },
      url: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      description: {},
      flow: {},
      steps: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, flowGroup as any);
    v$.value.$validate();

    return {
      flowGroupService,
      alertService,
      flowGroup,
      previousState,
      isSaving,
      currentLanguage,
      functionalFlows,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.flowGroup.id) {
        this.flowGroupService()
          .update(this.flowGroup)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A FlowGroup is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.flowGroupService()
          .create(this.flowGroup)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A FlowGroup is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
