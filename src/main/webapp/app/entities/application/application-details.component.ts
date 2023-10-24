import { defineComponent, inject, ref, watch, type Ref, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { type IApplication } from '@/shared/model/application.model';
import ApplicationService from './application.service';
import type AccountService from '@/account/account.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';
import { type ICapability } from '@/shared/model/capability.model';
import CapabilityComponent from '@/entities/capability/component/capability.vue';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type IFLowInterfaceLight, type PlantumlDTO } from '@/shared/model/plantuml-dto';
import { useStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationDetails',
  components: { CapabilityComponent },
  setup() {
    const applicationService = inject('applicationService', () => new ApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const application: Ref<IApplication> = ref({});

    const plantUMLImage = ref('');
    const applicationStructurePlantUMLImage = ref('');
    const capabilitiesPlantUMLImage = ref('');
    const interfaces: Ref<IFLowInterfaceLight[]> = ref([]);
    const flows: Ref<IFunctionalFlow[]> = ref([]);
    const consolidatedCapabilities: Ref<ICapability> = ref({});

    const layout = ref('elk');
    const refreshingPlantuml = ref(false);
    const groupComponents = ref(true);

    const lco: ICapability = {};
    const showLabels = ref(false);
    const interfaceCurrentPage = ref(1);
    const interfaceFilter = ref('');
    const interfacePerPage = ref(10);
    const flowCurrentPage = ref(1);
    const flowFilter = ref('');
    const flowPerPage = ref(10);
    const showLabelIfNumberapplicationsLessThan = ref(20);

    const tabIndex = ref(0);
    const applicationId = ref(-1);
    const sessionKey = 'application.detail.tab';

    watch(tabIndex, (newtab, oldtab) => {
      if (application.value && application.value.id) {
        tabIndex.value = newtab;
        sessionStorage.setItem(sessionKey, application.value.id + '#' + tabIndex.value);
      }
    });

    function created() {
      // https://github.com/bootstrap-vue/bootstrap-vue/issues/2803
      nextTick(() => {
        loadTab(applicationId.value);
      });
    }

    function loadTab(_applicationID: number) {
      if (sessionStorage.getItem(sessionKey)) {
        const parts = sessionStorage.getItem(sessionKey).split('#');
        const appliId = parseInt(parts[0]);
        const _tabIndex = parseInt(parts[1]);
        //const applicationID = parseInt(_applicationID);
        const applicationID = _applicationID;
        if (applicationID === appliId) {
          tabIndex.value = _tabIndex;
        } else {
          sessionStorage.removeItem(sessionKey);
          tabIndex.value = 0;
        }
      } else {
        tabIndex.value = 0;
      }
    }

    router.beforeEach((to, from, next) => {
      next(vm => {
        if (to.params.applicationId) {
          retrieveApplication(to.params.applicationId);
          applicationId.value = parseInt(to.params.applicationId);
        }
      });
    });

    const retrieveApplication = async applicationId => {
      try {
        plantUMLImage.value = '';
        capabilitiesPlantUMLImage.value = '';
        const res = await applicationService().find(applicationId);

        application.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        getPlantUML(applicationId);
        getStructurePlantUML(applicationId);
        retrieveCapabilities(applicationId);
      }
    };

    if (route.params?.applicationId) {
      retrieveApplication(route.params.applicationId);
    }

    const previousState = () => router.go(-1);

    async function getPlantUML(applicationId) {
      try {
        const res: PlantumlDTO = await applicationService().getPlantUML(
          applicationId,
          layout.value,
          groupComponents.value,
          showLabels.value,
          showLabelIfNumberapplicationsLessThan.value,
        );
        plantUMLImage.value = res.svg;
        interfaces.value = res.interfaces;
        flows.value = res.flows;
        refreshingPlantuml.value = false;
        showLabels.value = res.labelsShown;
      } catch (err) {
        console.log(err);
      }
    }

    function getStructurePlantUML(applicationId) {
      applicationService()
        .getApplicationStructurePlantUML(applicationId)
        .then(
          res => {
            applicationStructurePlantUMLImage.value = res.data;
          },
          err => {
            console.log(err);
          },
        );
    }

    function isOwner(application: IApplication): boolean {
      const username = store.account?.login ?? '';
      if (accountService.writeAuthorities) {
        return true;
      }
      if (application.owner && application.owner.users) {
        for (const user of application.owner.users) {
          if (user.login === username) {
            return true;
          }
        }
      }
      return false;
    }

    function retrieveCapabilities(applicationId) {
      applicationService()
        .getCapabilities(applicationId)
        .then(res => {
          consolidatedCapabilities.value = res;
          lco.value = consolidatedCapabilities.value;
        })
        .catch(error => {
          alertService.showHttpError(error.response);
        });
    }

    function routeToCapability(capId: string) {
      router.push({ name: 'CapabilityNavigate', params: { capabilityId: capId } });
    }

    function changeLayout() {
      if (layout.value == 'smetana') {
        layout.value = 'elk';
      } else {
        layout.value = 'smetana';
      }
      getPlantUML(application.value.id);
    }

    function doGroupComponents() {
      refreshingPlantuml.value = true;
      groupComponents.value = !groupComponents.value;
      getPlantUML(application.value.id);
    }

    function doShowLabels() {
      refreshingPlantuml.value = true;
      showLabelIfNumberapplicationsLessThan.value = -1; // if left to 15, hide wont work
      showLabels.value = !showLabels.value;
      getPlantUML(application.value.id);
    }

    return {
      alertService,
      application,

      previousState,
      retrieveApplication,
      interfaceFilter,
      interfaceCurrentPage,
      interfaces,
      interfacePerPage,
      tabIndex,
      plantUMLImage,
      showLabels,
      refreshingPlantuml,
      groupComponents,
      doShowLabels,
      doGroupComponents,
      changeLayout,
      applicationStructurePlantUMLImage,
      flowCurrentPage,
      flowFilter,
      flowPerPage,
      flows,
      consolidatedCapabilities,
      lco,
      routeToCapability,
      isOwner,
      accountService,
    };
  },
});
