import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FlowGroupService from './flow-group.service';
import { type IFlowGroup } from '@/shared/model/flow-group.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowGroupDetails',
  setup() {
    const flowGroupService = inject('flowGroupService', () => new FlowGroupService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const flowGroup: Ref<IFlowGroup> = ref({});

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

    return {
      alertService,
      flowGroup,

      previousState,
    };
  },
});
