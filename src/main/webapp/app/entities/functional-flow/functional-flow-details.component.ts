import { defineComponent, inject, ref, watch, type Ref, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import FunctionalFlowService from './functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { useAlertService } from '@/shared/alert/alert.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FunctionalFlowDetails',
  setup() {
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();

    const interfaces: Ref<IFlowInterface[]> = ref([]);

    const checkedInterface: Ref<IFlowInterface[]> = ref([]);

    const sequenceDiagram = ref(true);

    const functionalFlow: Ref<IFunctionalFlow> = ref({});
    const plantUMLImage = ref('');

    const searchSourceName = ref('');
    const searchTargetName = ref('');
    const searchProtocolName = ref('');

    const toBeSaved = ref(false);
    const searchDone = ref(false);

    const indexStepToDetach: Ref<number> = ref(-1);

    const isFetching = ref(false);

    const applicationIds = [];

    //////////////////////////////
    // Store current tab in sessionStorage
    //////////////////////////////

    const tabIndex = ref(0);
    const flowId = ref(-1);
    const sessionKey = 'flow.detail.tab';

    watch(tabIndex, (newtab, oldtab) => {
      if (functionalFlow.value && functionalFlow.value.id) {
        tabIndex.value = newtab;
        sessionStorage.setItem(sessionKey, functionalFlow.value.id + '#' + tabIndex.value);
      }
    });

    function created() {
      // https://github.com/bootstrap-vue/bootstrap-vue/issues/2803
      nextTick(() => {
        loadTab(flowId.value);
      });
    }

    function loadTab(_landscapeID) {
      if (sessionStorage.getItem(this.sessionKey)) {
        const parts = sessionStorage.getItem(sessionKey).split('#');
        const landId = parseInt(parts[0]);
        const _tabIndex = parseInt(parts[1]);
        const landscapeID = parseInt(_landscapeID);
        if (landscapeID === landId) {
          tabIndex.value = _tabIndex;
        } else {
          sessionStorage.removeItem(sessionKey);
          tabIndex.value = 1;
        }
      } else {
        tabIndex.value = 1;
      }
    }

    function changeDiagram() {
      isFetching.value = true;
      sequenceDiagram.value = !sequenceDiagram.value;
      getPlantUML(functionalFlow.value.id);
    }

    const retrieveFunctionalFlow = async functionalFlowId => {
      try {
        const res = await functionalFlowService().find(functionalFlowId);
        functionalFlow.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
      getPlantUML(functionalFlowId);
    };

    if (route.params?.functionalFlowId) {
      retrieveFunctionalFlow(route.params.functionalFlowId);
    }

    const previousState = () => router.go(-1);
    function getPlantUML(functionalFlowId) {
      console.log('Entering in method getPlantUML');
      functionalFlowService()
        .getPlantUML(functionalFlowId, sequenceDiagram.value)
        .then(
          res => {
            plantUMLImage.value = res.data;
            isFetching.value = false;
          },
          err => {
            console.log(err);
          },
        );
    }

    function exportPlantUML() {
      functionalFlowService()
        .getPlantUMLSource(functionalFlow.value.id, sequenceDiagram.value)
        .then(response => {
          const url = URL.createObjectURL(
            new Blob([response.data], {
              type: 'text/plain',
            }),
          );
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', functionalFlow.value.alias + '-plantuml.txt');
          document.body.appendChild(link);
          link.click();
        });
    }

    return {
      alertService,
      functionalFlow,
      previousState,
      tabIndex,
      plantUMLImage,
      exportPlantUML,
      isFetching,
      sequenceDiagram,
      getPlantUML,
      changeDiagram,
      accountService,
    };
  },
});
