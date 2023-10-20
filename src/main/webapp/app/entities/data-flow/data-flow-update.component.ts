import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DataFlowService from './data-flow.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import DataFormatService from '@/entities/data-format/data-format.service';
import { type IDataFormat } from '@/shared/model/data-format.model';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { type IDataFlow, DataFlow } from '@/shared/model/data-flow.model';
import { Frequency } from '@/shared/model/enumerations/frequency.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowUpdate',
  setup() {
    const dataFlowService = inject('dataFlowService', () => new DataFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataFlow: Ref<IDataFlow> = ref(new DataFlow());

    const dataFormatService = inject('dataFormatService', () => new DataFormatService());

    const dataFormats: Ref<IDataFormat[]> = ref([]);

    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());

    const functionalFlows: Ref<IFunctionalFlow[]> = ref([]);

    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());

    const flowInterfaces: Ref<IFlowInterface[]> = ref([]);
    const frequencyValues: Ref<string[]> = ref(Object.keys(Frequency));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDataFlow = async dataFlowId => {
      try {
        const res = await dataFlowService().find(dataFlowId);
        dataFlow.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dataFlowId) {
      retrieveDataFlow(route.params.dataFlowId);
    }

    const initRelationships = () => {
      dataFormatService()
        .retrieve()
        .then(res => {
          dataFormats.value = res.data;
        });
      functionalFlowService()
        .retrieve()
        .then(res => {
          functionalFlows.value = res.data;
        });
      flowInterfaceService()
        .retrieve()
        .then(res => {
          flowInterfaces.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      resourceName: {
        required: validations.required('This field is required.'),
      },
      resourceType: {},
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      frequency: {},
      contractURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      items: {},
      format: {},
      functionalFlows: {},
      flowInterface: {},
    };
    const v$ = useVuelidate(validationRules, dataFlow as any);
    v$.value.$validate();

    return {
      dataFlowService,
      alertService,
      dataFlow,
      previousState,
      frequencyValues,
      isSaving,
      currentLanguage,
      dataFormats,
      functionalFlows,
      flowInterfaces,
      v$,
    };
  },
  created(): void {
    this.dataFlow.functionalFlows = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dataFlow.id) {
        this.dataFlowService()
          .update(this.dataFlow)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A DataFlow is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dataFlowService()
          .create(this.dataFlow)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A DataFlow is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
