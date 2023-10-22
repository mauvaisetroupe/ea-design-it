import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DataFlowItemService from './data-flow-item.service';
import { type IDataFlowItem } from '@/shared/model/data-flow-item.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowItemDetails',
  setup() {
    const dataFlowItemService = inject('dataFlowItemService', () => new DataFlowItemService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject('accountService', () => new AccountService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dataFlowItem: Ref<IDataFlowItem> = ref({});

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

    return {
      alertService,
      dataFlowItem,

      previousState,
    };
  },
});
