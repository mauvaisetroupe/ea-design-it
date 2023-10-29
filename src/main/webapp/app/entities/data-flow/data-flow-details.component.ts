import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DataFlowService from './data-flow.service';
import { type IDataFlow } from '@/shared/model/data-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowDetails',
  setup() {
    const dataFlowService = inject('dataFlowService', () => new DataFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dataFlow: Ref<IDataFlow> = ref({});

    const retrieveDataFlow = async dataFlowId => {
      try {
        const res = await dataFlowService().find(dataFlowId);
        dataFlow.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.dataFlowId) {
      retrieveDataFlow(route.params.dataFlowId);
    }

    return {
      alertService,
      dataFlow,
      accountService,
      previousState,
    };
  },
});
