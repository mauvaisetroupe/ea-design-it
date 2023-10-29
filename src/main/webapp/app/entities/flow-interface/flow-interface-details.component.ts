import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FlowInterfaceService from './flow-interface.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import DataFlowService from '@/entities/data-flow/data-flow.service';
import { type IDataFlow } from '@/shared/model/data-flow.model';
import { useStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterfaceDetails',
  setup() {
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');
    const dataFlowService = inject('dataFlowService', () => new DataFlowService());

    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const previousState = () => router.go(-1);

    const flowInterface: Ref<IFlowInterface> = ref({});

    const retrieveFlowInterface = async flowInterfaceId => {
      try {
        const res = await flowInterfaceService().find(flowInterfaceId);
        flowInterface.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.flowInterfaceId) {
      retrieveFlowInterface(route.params.flowInterfaceId);
    }

    // function prepareToDetach(dataFlow: IDataFlow) {
    //   if (<any>this.$refs.detachDataEntity) {
    //     (<any>this.$refs.detachDataEntity).show();
    //   }
    //   this.dataFlowToDetach = dataFlow;
    // }

    // function detachDataFlow() {
    //   this.dataFlowService()
    //     .update(this.dataFlowToDetach)
    //     .then(res => {
    //       this.retrieveFlowInterface(this.flowInterface.id);
    //       this.closeDetachDialog();
    //     });
    // }

    // function closeDetachDialog(): void {
    //   (<any>this.$refs.detachDataEntity).hide();
    // }

    function isOwner(flowInterface: IFlowInterface): boolean {
      const username = store.account?.login ?? '';
      if (accountService.writeAuthorities) {
        return true;
      }
      if (flowInterface.owner && flowInterface.owner.users) {
        for (const user of flowInterface.owner.users) {
          if (user.login === username) {
            return true;
          }
        }
      }
      return false;
    }

    // function addNew() {
    //   console.log('not implemented');
    // }

    return {
      alertService,
      flowInterface,
      accountService,
      previousState,
      isOwner,
    };
  },
});
