import { computed, defineComponent, inject, ref, watch, type Ref, nextTick, onMounted } from 'vue';

import { useRoute, useRouter } from 'vue-router';
import useDataUtils from '@/shared/data/data-utils.service';

import type { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import type { FlowImport, IFlowImport } from '@/shared/model/flow-import.model';
import type { FlowInterface, IFlowInterface } from '@/shared/model/flow-interface.model';
import type { FunctionalFlow } from '@/shared/model/functional-flow.model';
import type { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import type { IFunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import { FunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import FlowImportService from '@/entities/flow-import/flow-import.service';
import type { ICapability } from '@/shared/model/capability.model';
import CapabilityComponent from '@/entities/capability/component/capability.vue';
import { type IApplication } from '@/shared/model/application.model';
import BusinessAndDataObjectFullpath from '@/eadesignit/components/business-data-object-fullpath.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LandscapeViewDetails',
  components: { CapabilityComponent, BusinessAndDataObjectFullpath },
  setup() {
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');
    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());
    const functionalFlowStepService = inject('functionalFlowStepService', () => new FunctionalFlowStepService());
    const flowImportService = inject('flowImportService', () => new FlowImportService());

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const landscapeView: Ref<ILandscapeView> = ref({});
    const consolidatedCapability: Ref<ICapability> = ref({});
    const plantUMLImage = ref('');
    const plantUMLDataObjectsLandscapeImage = ref('');

    const drawIoSVG = ref('');
    const isHidden = ref(true);
    const isEditing = ref(false);
    const drawIOToBeSaved = ref(false);
    const emptySteps = ref([]);
    const functionalFlows: Ref<FunctionalFlow[]> = ref([]);
    const toBeSaved = ref(false);
    const flowToDetach: Ref<number> = ref(-1);
    const layout = ref('elk');
    const refreshingPlantuml = ref(false);
    const refreshingDataObjectsLandscapePlantuml = ref(false);
    const groupComponents = ref(true);
    const showLabels = ref(false);
    const tabIndex = ref(1);
    const sessionKey = 'landscape.detail.tab';
    const landscapeViewId = ref(-1);
    const filter = ref('');
    const showLabelIfNumberapplicationsLessThan = ref(20);
    const applicationsOnlyInCapabilities: Ref<IApplication[]> = ref([]);
    const applicationsOnlyInFlows: Ref<IApplication[]> = ref([]);
    const reportToDisplay = computed(() => {
      return applicationsOnlyInCapabilities.value.length > 0 || applicationsOnlyInFlows.value.length > 0;
    });
    const capabilitiesByApplicationID = ref(new Object());
    const flowsByApplicationID = ref(new Object());

    //for description update
    const reorderAliasflowToSave: Ref<IFunctionalFlow[]> = ref([]);
    //for reordering update
    const reorderStepToSave: Ref<IFunctionalFlowStep[]> = ref([]);

    const allAlias = computed(() => {
      return landscapeView.value.flows.map(f => f.alias);
    });

    const allApplications = computed(() => {
      if (!landscapeView.value || !landscapeView.value.flows) return [];
      const allApplicationWithDuplicates = landscapeView.value.flows.flatMap(f => f.allApplications);
      if (allApplicationWithDuplicates?.length > 0) {
        allApplicationWithDuplicates.filter((value, index, self) => index === self.findIndex(a => a.id === value.id));
      }
      return allApplicationWithDuplicates;
    });

    const onlyCapabilitiesPercent = computed(() => {
      return ((100 * applicationsOnlyInCapabilities.value.length) / allApplications.value.length).toFixed(0);
    });

    const onlyFlowsPercent = computed(() => {
      return ((100 * applicationsOnlyInFlows.value.length) / allApplications.value.length).toFixed(0);
    });

    //@Watch('tabIndex')
    watch(tabIndex, (newtab, oldtab) => {
      tabIndex.value = newtab;
      if (landscapeView.value && landscapeView.value.id) {
        sessionStorage.setItem(sessionKey, landscapeView.value.id + '#' + tabIndex.value);
      }
    });

    function loadTab(_landscapeID) {
      console.log('loadTab' + _landscapeID);
      if (sessionStorage.getItem(sessionKey)) {
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

    const retrieveLandscapeView = async landscapeViewId => {
      try {
        loadTab(landscapeViewId);
        const res = await landscapeViewService().find(landscapeViewId);
        const landscape = res.landscape;
        conputeAllApplicationsByFlow(landscape);
        if (landscapeView.value && landscapeView.value.flows) {
          // flowsByApplicationID
          landscapeView.value.flows.forEach(f => {
            f.steps
              .map(s => s.flowInterface)
              .forEach(i => {
                if (!flowsByApplicationID.value[i.source.id]) flowsByApplicationID.value[i.source.id] = [];
                flowsByApplicationID.value[i.source.id] = [f];
                if (!flowsByApplicationID.value[i.target.id]) flowsByApplicationID.value[i.target.id] = [];
                flowsByApplicationID.value[i.target.id] = [f];
              });
          });
        }
        landscapeView.value = landscape;
        consolidatedCapability.value = res.consolidatedCapability;

        if (landscapeView.value && landscapeView.value.capabilityApplicationMappings) {
          // capabilitiesByApplicationID
          landscapeView.value.capabilityApplicationMappings.forEach(cm => {
            if (!capabilitiesByApplicationID.value[cm.application.id]) capabilitiesByApplicationID.value[cm.application.id] = [];
            capabilitiesByApplicationID.value[cm.application.id].push(cm.capability);
          });
        }

        applicationsOnlyInCapabilities.value = res.applicationsOnlyInCapabilities;
        applicationsOnlyInFlows.value = res.applicationsOnlyInFlows;
        if (landscapeView.value.compressedDrawSVG) {
          drawIoSVG.value = decodeURIComponent(escape(window.atob(landscapeView.value.compressedDrawSVG)));
        }
      } catch (error) {
        alertService.showAnyError(error);
      }
      getPlantUML(landscapeViewId);
      getDataObjectsLandscapePlantUML(landscapeViewId);
    };

    if (route.params?.landscapeViewId) {
      retrieveLandscapeView(route.params.landscapeViewId);
      landscapeViewId.value = parseInt(route.params?.landscapeViewId);
    }

    const previousState = () => router.go(-1);

    function getPlantUML(landscapeViewId) {
      refreshingPlantuml.value = true;
      landscapeViewService()
        .getPlantUML(landscapeViewId, layout.value, groupComponents.value, showLabels.value, showLabelIfNumberapplicationsLessThan.value)
        .then(
          res => {
            plantUMLImage.value = res.svg;
            showLabels.value = res.labelsShown;
            refreshingPlantuml.value = false;
          },
          err => {
            console.log(err);
          },
        );
    }

    function getDataObjectsLandscapePlantUML(landscapeViewId) {
      refreshingDataObjectsLandscapePlantuml.value = true;
      landscapeViewService()
        .getDataObjectLandscapePlantUML(landscapeViewId)
        .then(
          res => {
            plantUMLDataObjectsLandscapeImage.value = res;
            refreshingDataObjectsLandscapePlantuml.value = false;
          },
          err => {
            console.log(err);
          },
        );
    }
    onMounted(async () => {
      window.addEventListener('message', receiveMessage);
    });

    function editDiagram(): void {
      if (!landscapeView.value.compressedDrawXML) {
        landscapeViewService()
          .getDrawIO(landscapeView.value.id)
          .then(
            res => {
              landscapeView.value.compressedDrawXML = res.data;
              isHidden.value = false;
              isEditing.value = true;
            },
            err => {
              console.log(err);
            },
          );
      } else {
        isHidden.value = false;
        isEditing.value = true;
      }
    }

    function saveDiagram(): void {
      const dto: ILandscapeView = {};
      dto.id = landscapeView.value.id;
      dto.compressedDrawXML = landscapeView.value.compressedDrawXML;
      console.log(drawIoSVG.value);
      dto.compressedDrawSVG = window.btoa(unescape(encodeURIComponent(drawIoSVG.value)));
      landscapeViewService()
        .partialUpdate(dto)
        .then(
          res => {
            drawIOToBeSaved.value = false;
          },
          err => {
            alertService.showAnyError(err);
          },
        );
    }

    function deleteDiagram(): void {
      landscapeViewService()
        .deleteDrawInformation(landscapeView.value.id)
        .then(
          res => {
            drawIOToBeSaved.value = false;
            drawIoSVG.value = '';
          },
          err => {
            alertService.showAnyError(err);
          },
        );
      (<any>this.$refs.removeDiagramEntity).hide();
    }

    function receiveMessage(evt) {
      const iframe: HTMLIFrameElement = document.getElementById('myDiv') as HTMLIFrameElement;
      //console.log(evt)
      if (evt.data.length > 0) {
        const msg = JSON.parse(evt.data);
        if (msg.event == 'init') {
          //iframe.contentWindow.postMessage(JSON.stringify({action: 'load',autosave: 1, xml: '<mxGraphModel dx="1350" dy="793" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="850" pageHeight="1100" math="0" shadow="0"><root><mxCell id="0" /><mxCell id="1" parent="0" /><mxCell id="vISkdTzWN7_PVdaD8Uo9-1" value="" style="ellipse;whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="310" y="170" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-2" value="TEST" style="whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="140" y="250" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-3" value="" style="endArrow=classic;html=1;rounded=0;exitX=1;exitY=0.5;exitDx=0;exitDy=0;entryX=0;entryY=1;entryDx=0;entryDy=0;" parent="1" source="vISkdTzWN7_PVdaD8Uo9-2" target="vISkdTzWN7_PVdaD8Uo9-1" edge="1"><mxGeometry width="50" height="50" relative="1" as="geometry"><mxPoint x="400" y="430" as="sourcePoint" /><mxPoint x="450" y="380" as="targetPoint" /></mxGeometry></mxCell></root></mxGraphModel>'}), '*');
          iframe.contentWindow.postMessage(
            JSON.stringify({ action: 'load', autosave: 1, xml: landscapeView.value.compressedDrawXML }),
            '*',
          );
        } else if (msg.event == 'export') {
          // Extracts SVG DOM from data URI to enable links
          const svg = atob(msg.data.substring(msg.data.indexOf(',') + 1));
          drawIoSVG.value = svg;
          isHidden.value = true;
          isEditing.value = false;
          drawIOToBeSaved.value = true;
        } else if (msg.event == 'save') {
          iframe.contentWindow.postMessage(
            JSON.stringify({ action: 'export', format: 'xmlsvg', xml: msg.xml, spin: 'Updating page' }),
            '*',
          );
          console.log(msg.xml);
          drawIOToBeSaved.value = true;
          landscapeView.value.compressedDrawXML = msg.xml;
        } else if (msg.event == 'exit') {
          isHidden.value = true;
          isEditing.value = false;
        }
      }
    }

    function exportDrawIOXML() {
      const xmlContent = landscapeView.value.compressedDrawXML;

      const filename = 'draw-io-export-' + landscapeView.value.diagramName.replace(/ /g, '-') + '.xml';
      const pom = document.createElement('a');
      const bb = new Blob([xmlContent], { type: 'text/plain' });

      pom.setAttribute('href', window.URL.createObjectURL(bb));
      pom.setAttribute('download', filename);

      pom.dataset.downloadurl = ['text/plain', pom.download, pom.href].join(':');
      pom.draggable = true;
      pom.classList.add('dragout');

      pom.click();
    }

    const removeDiagramEntity = ref<any>(null);
    const detachFlowEntity = ref<any>(null);
    const addExistingEntity = ref<any>(null);

    function prepareRemove(): void {
      removeDiagramEntity.value.show();
    }

    function closeDialog(): void {
      removeDiagramEntity.value.hide();
    }

    function prepareToDetach(index: number) {
      flowToDetach.value = index;
      detachFlowEntity.value.show();
    }

    function detachFunctionalFlow() {
      landscapeView.value.flows.splice(flowToDetach.value, 1);
      landscapeViewService()
        .update(landscapeView.value)
        .then(res => {
          const landscape = res;
          conputeAllApplicationsByFlow(landscape);
          landscapeView.value = landscape;
          closeDetachDialog();
          getPlantUML(landscapeView.value.id);
        });
    }

    function closeDetachDialog(): void {
      detachFlowEntity.value.hide();
    }

    function openSearchFlow(): void {
      if (addExistingEntity.value) {
        functionalFlowService()
          .retrieve()
          .then(res => {
            console.log(res.data);
            functionalFlows.value = res.data.filter(f => f.alias != null);
            addExistingEntity.value.show();
          });
      }
    }

    function closeSearchFlow(): void {
      addExistingEntity.value.hide();
    }

    function addNew(functionalFlow: IFunctionalFlow): void {
      if (!landscapeView.value.flows) {
        landscapeView.value.flows = [];
      }
      landscapeView.value.flows.push(functionalFlow);
      toBeSaved.value = true;
      landscapeViewService()
        .update(landscapeView.value)
        .then(res => {
          const landscape = res;
          conputeAllApplicationsByFlow(landscape);
          landscapeView.value = landscape;
          closeSearchFlow();
          getPlantUML(landscapeView.value.id);
        });
    }

    function exportPlantUML() {
      landscapeViewService()
        .getPlantUMLSource(landscapeView.value.id)
        .then(response => {
          const url = URL.createObjectURL(
            new Blob([response.data], {
              type: 'text/plain',
            }),
          );
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', landscapeView.value.diagramName + '-plantuml.txt');
          document.body.appendChild(link);
          link.click();
        });
    }

    function changeLayout() {
      if (layout.value == 'smetana') {
        layout.value = 'elk';
      } else {
        layout.value = 'smetana';
      }
      getPlantUML(landscapeView.value.id);
    }

    function doGroupComponents() {
      refreshingPlantuml.value = true;
      groupComponents.value = !groupComponents.value;
      getPlantUML(landscapeView.value.id);
    }

    function doShowLabels() {
      refreshingPlantuml.value = true;
      showLabels.value = !showLabels.value;
      showLabelIfNumberapplicationsLessThan.value = -1; // if left to 15, hide wont work
      getPlantUML(landscapeView.value.id);
    }

    const filteredRows = computed(() => {
      return functionalFlows.value.filter(row => {
        const alias = row.alias ? row.alias.toString().toLowerCase() : '';
        const id = row.id.toString().toLowerCase();
        const description = row.description ? row.description.toString().toLowerCase() : '';
        const inFFF = row.steps
          ? row.steps
              .map(i => i.flowInterface)
              .map(i => i.alias)
              .join(' ')
              .toString()
              .toLowerCase()
          : '';
        const searchTerm = filter.value.toLowerCase();

        return alias.includes(searchTerm) || id.includes(searchTerm) || inFFF.includes(searchTerm) || description.includes(searchTerm);
      });
    });

    function conputeAllApplicationsByFlow(landscape: ILandscapeView) {
      landscape.flows.forEach(flow => {
        const distinctApplications: Record<string, IApplication> = {};
        flow.steps
          .map(step => step.flowInterface)
          .forEach(inter => {
            if (inter.source) {
              distinctApplications[inter.source.alias] = inter.source;
            }
            if (inter.target) {
              distinctApplications[inter.target.alias] = inter.target;
            }
          });
        flow.allApplications = Object.values(distinctApplications);
      });
    }

    return {
      alertService,
      landscapeView,
      ...dataUtils,
      previousState,
      plantUMLImage,
      changeLayout,
      refreshingPlantuml,
      exportPlantUML,
      doGroupComponents,
      groupComponents,
      doShowLabels,
      showLabels,
      accountService,
      prepareToDetach,
      openSearchFlow,
      consolidatedCapability,
      drawIoSVG,
      editDiagram,
      drawIOToBeSaved,
      saveDiagram,
      prepareRemove,
      deleteDiagram,
      exportDrawIOXML,
      isHidden,
      applicationsOnlyInCapabilities,
      onlyCapabilitiesPercent,
      isEditing,
      flowsByApplicationID,
      capabilitiesByApplicationID,
      applicationsOnlyInFlows,
      onlyFlowsPercent,
      tabIndex,
      closeDialog,
      detachFunctionalFlow,
      closeDetachDialog,
      functionalFlows,
      filter,
      filteredRows,
      addNew,
      closeSearchFlow,
      layout,
      removeDiagramEntity,
      addExistingEntity,
      detachFlowEntity,
      refreshingDataObjectsLandscapePlantuml,
      plantUMLDataObjectsLandscapeImage,
    };
  },
});
