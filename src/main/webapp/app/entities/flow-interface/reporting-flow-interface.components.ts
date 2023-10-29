import { defineComponent, getCurrentInstance, inject, onMounted, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import ReportingService from '@/eadesignit/reporting.service';
import { DataFlow } from '@/shared/model/data-flow.model';
import type { bvToast } from 'bootstrap-vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterface',
  setup() {
    const route = useRoute();
    const router = useRouter();

    const accountService = inject<AccountService>('accountService');
    const alertService = inject('alertService', () => useAlertService(), true);
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const reportingService = inject('reportingService', () => new ReportingService());

    const interfaceToKeep: Ref<IFlowInterface> = ref({});
    const interfacesToMerge: Ref<IFlowInterface[]> = ref([]);
    const checkToMerge: Ref<string[]> = ref([]);
    const flowInterfaces: Ref<IFlowInterface[]> = ref([]);
    const isFetching = ref(false);
    const mergeEntity = ref<any>(null);

    onMounted(async () => {
      await retrieveAllFlowInterfaces();
    });

    const retrieveAllFlowInterfaces = async () => {
      isFetching.value = true;
      try {
        const res = await reportingService().retrieveInterfaces();
        flowInterfaces.value = res.data;
        let mycolor = 'mycolor';
        let previousTuple = '';
        let mergeList = [];
        flowInterfaces.value.forEach(element => {
          if (previousTuple !== element.source.name + element.target.name + element.protocol.id) {
            mergeList = [];
            if (mycolor === 'mycolor') {
              mycolor = '';
            } else {
              mycolor = 'mycolor';
            }
          }
          mergeList.push(element.alias);
          previousTuple = element.source.name + element.target.name + element.protocol.id;
          (element as any).colored = mycolor;
          (element as any).mergeList = mergeList;
        });
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    function clear(): void {
      retrieveAllFlowInterfaces();
    }

    function handleSyncList(): void {
      clear();
    }

    const closeMergeDialog = () => {
      mergeEntity.value.hide();
    };

    function prepareMerge(instance: IFlowInterface): void {
      interfaceToKeep.value = instance;
      const aliasToMerge = (interfaceToKeep.value as any).mergeList as string[];
      console.log(aliasToMerge);

      interfacesToMerge.value = [];
      checkToMerge.value = [];

      flowInterfaces.value.forEach(inter => {
        if (aliasToMerge.indexOf(inter.alias) > -1) {
          interfacesToMerge.value.push(inter);
          if (inter.id != interfaceToKeep.value.id) {
            checkToMerge.value.push(inter.alias);
          }
        }
      });

      if (mergeEntity.value) {
        mergeEntity.value.show();
      }
    }

    const mergeFlowInterface = async () => {
      isFetching.value = true;
      try {
        const res = await reportingService().mergeInterfaces(interfaceToKeep.value, checkToMerge.value);
        const message = 'FlowInterfaces ' + checkToMerge.value + ' have been merged and replaced by ' + interfaceToKeep.value.alias;
        alertService.showSuccess(message);
        interfaceToKeep.value = {};
        interfacesToMerge.value = [];
        checkToMerge.value = [];
        retrieveAllFlowInterfaces();
        closeMergeDialog();
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    return {
      accountService,
      handleSyncList,
      isFetching,
      flowInterfaces,
      prepareMerge,
      interfaceToKeep,
      checkToMerge,
      interfacesToMerge,
      closeMergeDialog,
      mergeFlowInterface,
      mergeEntity,
    };
  },
});
