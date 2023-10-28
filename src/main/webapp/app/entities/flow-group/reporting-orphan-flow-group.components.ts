import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import { IFlowGroup } from '@/shared/model/flow-group.model';
import ReportingService from '@/eadesignit/reporting.service';
import AlertService from '@/shared/alert/alert.service';
import FlowGroupService from './flow-group.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowGroup',
  setup() {
    const reportingService = inject('reportingService', () => new ReportingService());
    const flowGroupService = inject('flowGroupService', () => new FlowGroupService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const isFetching = ref(false);
    const flowGroups: Ref<IFlowGroup[]> = ref([]);

    function clear(): void {
      retrieveAllFlowGroups();
    }

    onMounted(async () => {
      await retrieveAllFlowGroups();
    });

    const retrieveAllFlowGroups = async () => {
      isFetching.value = true;
      try {
        const res = await flowGroupService().retrieve();
        flowGroups.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    function handleSyncList(): void {
      clear();
    }

    return {
      flowGroups,
      handleSyncList,
      isFetching,
      clear,
    };
  },
});
