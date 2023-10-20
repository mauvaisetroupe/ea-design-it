import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DataFlowItemService from './data-flow-item.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import { type IDataFlow } from '@/shared/model/data-flow.model';
import { type IDataFlowItem, DataFlowItem } from '@/shared/model/data-flow-item.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowItemUpdate',
  setup() {
    const dataFlowItemService = inject('dataFlowItemService', () => new DataFlowItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataFlowItem: Ref<IDataFlowItem> = ref(new DataFlowItem());

    const dataFlowService = inject('dataFlowService', () => new DataFlowService());

    const dataFlows: Ref<IDataFlow[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDataFlowItem = async dataFlowItemId => {
      try {
        const res = await dataFlowItemService().find(dataFlowItemId);
        dataFlowItem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dataFlowItemId) {
      retrieveDataFlowItem(route.params.dataFlowItemId);
    }

    const initRelationships = () => {
      dataFlowService()
        .retrieve()
        .then(res => {
          dataFlows.value = res.data;
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
      contractURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      dataFlow: {},
    };
    const v$ = useVuelidate(validationRules, dataFlowItem as any);
    v$.value.$validate();

    return {
      dataFlowItemService,
      alertService,
      dataFlowItem,
      previousState,
      isSaving,
      currentLanguage,
      dataFlows,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dataFlowItem.id) {
        this.dataFlowItemService()
          .update(this.dataFlowItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A DataFlowItem is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dataFlowItemService()
          .create(this.dataFlowItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A DataFlowItem is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
