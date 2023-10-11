import { Component, Inject, Watch } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from '@/account/account.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import { FlowImport, IFlowImport } from '@/shared/model/flow-import.model';
import { FlowInterface, IFlowInterface } from '@/shared/model/flow-interface.model';
import { FunctionalFlow } from '@/shared/model/functional-flow.model';
import { IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { IFunctionalFlowStep, FunctionalFlowStep } from '@/shared/model/functional-flow-step.model';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import FlowImportService from '@/entities/flow-import/flow-import.service';
import { ICapability } from '@/shared/model/capability.model';
import CapabilityComponent from '@/entities/capability/component/capability.vue';
import { IApplication } from '@/shared/model/application.model';

@Component({
  components: {
    CapabilityComponent,
  },
})
export default class LandscapeViewDetails extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') public accountService: () => AccountService;
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;
  @Inject('functionalFlowStepService') private functionalFlowStepService: () => FunctionalFlowStepService;
  @Inject('flowImportService') private flowImportService: () => FlowImportService;

  public landscapeView: ILandscapeView = {};
  public consolidatedCapability: ICapability[] = [];
  public plantUMLImage = '';

  public drawIoSVG = '';
  public isHidden = true;
  public isEditing = false;
  public drawIOToBeSaved = false;
  public emptySteps = [];
  public functionalFlows: FunctionalFlow[] = [];
  public toBeSaved = false;
  public flowToDetach: number;
  public layout = 'elk';
  public refreshingPlantuml = false;
  public groupComponents = true;
  public showLabels = false;
  public tabIndex = 1;
  public sessionKey = 'landscape.detail.tab';
  public landscapeViewId = -1;
  public filter = '';
  public showLabelIfNumberapplicationsLessThan = 20;
  public applicationsOnlyInCapabilities: IApplication[] = [];
  public applicationsOnlyInFlows: IApplication[] = [];
  public get reportToDisplay() {
    return (this.applicationsOnlyInCapabilities.length > 0 || this.applicationsOnlyInFlows.length > 0);
  }
  public capabilitiesByApplicationID = new Object();
  public flowsByApplicationID = new Object();

  //for description update
  public reorderAliasflowToSave: IFunctionalFlow[] = [];
  //for reordering update
  public reorderStepToSave: IFunctionalFlowStep[] = [];

  public get allAlias() {
    return this.landscapeView.flows.map(f => f.alias);
  }

  public get allApplications() {
    if (!this.landscapeView || !this.landscapeView.flows) return [];
    return this.landscapeView.flows.flatMap(f => f.allApplications).filter((value, index, self) =>
    index === self.findIndex((a) => (
      a.id === value.id
    )))
  }

  public get onlyCapabilitiesPercent() {
    return (100 * this.applicationsOnlyInCapabilities.length / this.allApplications.length).toFixed(0)
  }

  public get onlyFlowsPercent() {
    return (100 * this.applicationsOnlyInFlows.length / this.allApplications.length).toFixed(0)
  }


  @Watch('tabIndex')
  public onTabChange(newtab) {
    this.tabIndex = newtab;
    if (this.landscapeView && this.landscapeView.id) {
      sessionStorage.setItem(this.sessionKey, this.landscapeView.id + '#' + this.tabIndex);
    }
  }

  public created() {
    // https://github.com/bootstrap-vue/bootstrap-vue/issues/2803
    this.$nextTick(() => {
      this.loadTab(this.landscapeViewId);
    });
  }

  public loadTab(_landscapeID) {
    if (sessionStorage.getItem(this.sessionKey)) {
      const parts = sessionStorage.getItem(this.sessionKey).split('#');
      const landId = parseInt(parts[0]);
      const tabIndex = parseInt(parts[1]);
      const landscapeID = parseInt(_landscapeID);
      if (landscapeID === landId) {
        this.tabIndex = tabIndex;
      } else {
        sessionStorage.removeItem(this.sessionKey);
        this.tabIndex = 1;
      }
    } else {
      this.tabIndex = 1;
    }
  }

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeViewId) {
        vm.retrieveLandscapeView(to.params.landscapeViewId);
        vm.landscapeViewId = parseInt(to.params.landscapeViewId);
      }
    });
  }

  public retrieveLandscapeView(landscapeViewId) {
    const step: FunctionalFlowStep = new FunctionalFlowStep();
    step.description = 'EMPTYSTEP';
    const inter: FlowInterface = new FlowInterface();
    step.flowInterface = inter;
    if (this.emptySteps.length == 0) this.emptySteps.push(step);
    this.landscapeViewService()
      .find(landscapeViewId)
      .then(res => {
        this.landscapeView = res.landscape;
        this.landscapeView.flows.forEach(flow => {
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
        if (this.landscapeView && this.landscapeView.flows) {
          // flowsByApplicationID
          this.landscapeView.flows.forEach(f => {
            f.steps.map(s => s.flowInterface).forEach(i => {
              if (!this.flowsByApplicationID[i.source.id]) this.flowsByApplicationID[i.source.id] = [];
              this.flowsByApplicationID[i.source.id] = [f];
              if (!this.flowsByApplicationID[i.target.id]) this.flowsByApplicationID[i.target.id] = [];
              this.flowsByApplicationID[i.target.id] = [f];
            })
          });
        }
        this.consolidatedCapability = res.consolidatedCapability;

        if (this.landscapeView && this.landscapeView.capabilityApplicationMappings) {
          // capabilitiesByApplicationID
          this.landscapeView.capabilityApplicationMappings.forEach(cm => {
            if (!this.capabilitiesByApplicationID[cm.application.id]) this.capabilitiesByApplicationID[cm.application.id] = [];
            this.capabilitiesByApplicationID[cm.application.id].push(cm.capability);
          })
        }

        this.applicationsOnlyInCapabilities = res.applicationsOnlyInCapabilities;
        this.applicationsOnlyInFlows = res.applicationsOnlyInFlows
        if (this.landscapeView.compressedDrawSVG) {
          this.drawIoSVG = decodeURIComponent(escape(window.atob(this.landscapeView.compressedDrawSVG)));
        }
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.getPlantUML(landscapeViewId);
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getPlantUML(landscapeViewId) {
    this.refreshingPlantuml = true;
    this.landscapeViewService()
      .getPlantUML(landscapeViewId, this.layout, this.groupComponents, this.showLabels, this.showLabelIfNumberapplicationsLessThan)
      .then(
        res => {
          this.plantUMLImage = res.svg;
          this.showLabels = res.labelsShown;
          this.refreshingPlantuml = false;
        },
        err => {
          console.log(err);
        }
      );
  }

  public mounted() {
    window.addEventListener('message', this.receiveMessage);
  }

  public editDiagram(): void {
    if (!this.landscapeView.compressedDrawXML) {
      this.landscapeViewService()
        .getDrawIO(this.landscapeView.id)
        .then(
          res => {
            this.landscapeView.compressedDrawXML = res.data;
            this.isHidden = false;
            this.isEditing = true;
          },
          err => {
            console.log(err);
          }
        );
    } else {
      this.isHidden = false;
      this.isEditing = true;
    }
  }

  public saveDiagram(): void {
    const dto: ILandscapeView = {};
    dto.id = this.landscapeView.id;
    dto.compressedDrawXML = this.landscapeView.compressedDrawXML;
    console.log(this.drawIoSVG);
    dto.compressedDrawSVG = window.btoa(unescape(encodeURIComponent(this.drawIoSVG)));
    this.landscapeViewService()
      .partialUpdate(dto)
      .then(
        res => {
          this.drawIOToBeSaved = false;
        },
        err => {
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public deleteDiagram(): void {
    this.landscapeViewService()
      .deleteDrawInformation(this.landscapeView.id)
      .then(
        res => {
          this.drawIOToBeSaved = false;
          this.drawIoSVG = '';
        },
        err => {
          this.alertService().showHttpError(this, err.response);
        }
      );
    (<any>this.$refs.removeDiagramEntity).hide();
  }

  public receiveMessage(evt) {
    const iframe: HTMLIFrameElement = document.getElementById('myDiv') as HTMLIFrameElement;
    //console.log(evt)
    if (evt.data.length > 0) {
      const msg = JSON.parse(evt.data);
      if (msg.event == 'init') {
        //iframe.contentWindow.postMessage(JSON.stringify({action: 'load',autosave: 1, xml: '<mxGraphModel dx="1350" dy="793" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="850" pageHeight="1100" math="0" shadow="0"><root><mxCell id="0" /><mxCell id="1" parent="0" /><mxCell id="vISkdTzWN7_PVdaD8Uo9-1" value="" style="ellipse;whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="310" y="170" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-2" value="TEST" style="whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="140" y="250" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-3" value="" style="endArrow=classic;html=1;rounded=0;exitX=1;exitY=0.5;exitDx=0;exitDy=0;entryX=0;entryY=1;entryDx=0;entryDy=0;" parent="1" source="vISkdTzWN7_PVdaD8Uo9-2" target="vISkdTzWN7_PVdaD8Uo9-1" edge="1"><mxGeometry width="50" height="50" relative="1" as="geometry"><mxPoint x="400" y="430" as="sourcePoint" /><mxPoint x="450" y="380" as="targetPoint" /></mxGeometry></mxCell></root></mxGraphModel>'}), '*');
        iframe.contentWindow.postMessage(JSON.stringify({ action: 'load', autosave: 1, xml: this.landscapeView.compressedDrawXML }), '*');
      } else if (msg.event == 'export') {
        // Extracts SVG DOM from data URI to enable links
        const svg = atob(msg.data.substring(msg.data.indexOf(',') + 1));
        this.drawIoSVG = svg;
        this.isHidden = true;
        this.isEditing = false;
        this.drawIOToBeSaved = true;
      } else if (msg.event == 'save') {
        iframe.contentWindow.postMessage(JSON.stringify({ action: 'export', format: 'xmlsvg', xml: msg.xml, spin: 'Updating page' }), '*');
        console.log(msg.xml);
        this.drawIOToBeSaved = true;
        this.landscapeView.compressedDrawXML = msg.xml;
      } else if (msg.event == 'exit') {
        this.isHidden = true;
        this.isEditing = false;
      }
    }
  }

  public exportDrawIOXML() {
    const xmlContent = this.landscapeView.compressedDrawXML;

    const filename = 'draw-io-export-' + this.landscapeView.diagramName.replace(/ /g, '-') + '.xml';
    const pom = document.createElement('a');
    const bb = new Blob([xmlContent], { type: 'text/plain' });

    pom.setAttribute('href', window.URL.createObjectURL(bb));
    pom.setAttribute('download', filename);

    pom.dataset.downloadurl = ['text/plain', pom.download, pom.href].join(':');
    pom.draggable = true;
    pom.classList.add('dragout');

    pom.click();
  }

  public prepareRemove(): void {
    if (<any>this.$refs.removeDiagramEntity) {
      (<any>this.$refs.removeDiagramEntity).show();
    }
  }

  public closeDialog(): void {
    (<any>this.$refs.removeDiagramEntity).hide();
  }

  public prepareToDetach(index: number) {
    if (<any>this.$refs.detachFlowEntity) {
      (<any>this.$refs.detachFlowEntity).show();
    }
    this.flowToDetach = index;
  }

  public detachFunctionalFlow() {
    this.landscapeView.flows.splice(this.flowToDetach, 1);
    this.landscapeViewService()
      .update(this.landscapeView)
      .then(res => {
        this.landscapeView = res;
        this.closeDetachDialog();
        this.getPlantUML(this.landscapeView.id);
      });
  }

  public closeDetachDialog(): void {
    (<any>this.$refs.detachFlowEntity).hide();
  }

  public openSearchFlow(): void {
    if (<any>this.$refs.addExistingEntity) {
      this.functionalFlowService()
        .retrieve()
        .then(res => {
          console.log(res.data);
          this.functionalFlows = res.data.filter(f => f.alias != null);
          (<any>this.$refs.addExistingEntity).show();
        });
    }
  }

  public closeSearchFlow(): void {
    (<any>this.$refs.addExistingEntity).hide();
  }

  public addNew(functionalFlow: IFunctionalFlow): void {
    if (!this.landscapeView.flows) {
      this.landscapeView.flows = [];
    }
    this.landscapeView.flows.push(functionalFlow);
    this.toBeSaved = true;
    this.landscapeViewService()
      .update(this.landscapeView)
      .then(res => {
        this.landscapeView = res;
        this.closeSearchFlow();
        this.getPlantUML(this.landscapeView.id);
      });
  }

  public exportPlantUML() {
    this.landscapeViewService()
      .getPlantUMLSource(this.landscapeView.id)
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'text/plain',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', this.landscapeView.diagramName + '-plantuml.txt');
        document.body.appendChild(link);
        link.click();
      });
  }

  public changeLayout() {
    if (this.layout == 'smetana') {
      this.layout = 'elk';
    } else {
      this.layout = 'smetana';
    }
    this.getPlantUML(this.landscapeView.id);
  }

  public doGroupComponents() {
    this.refreshingPlantuml = true;
    this.groupComponents = !this.groupComponents;
    this.getPlantUML(this.landscapeView.id);
  }

  public doShowLabels() {
    this.refreshingPlantuml = true;
    this.showLabels = !this.showLabels;
    this.showLabelIfNumberapplicationsLessThan = -1; // if left to 15, hide wont work
    this.getPlantUML(this.landscapeView.id);
  }

  get filteredRows() {
    return this.functionalFlows.filter(row => {
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
      const searchTerm = this.filter.toLowerCase();

      return alias.includes(searchTerm) || id.includes(searchTerm) || inFFF.includes(searchTerm) || description.includes(searchTerm);
    });
  }
}
