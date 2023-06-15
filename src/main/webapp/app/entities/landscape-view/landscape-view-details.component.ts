import { Component, Inject } from 'vue-property-decorator';

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

  public reorderAlias = false;

  public layout = 'elk';
  public refreshingPlantuml = false;
  public groupComponents = true;

  //for description update
  public reorderAliasflowToSave: IFunctionalFlow[] = [];
  //for reordering update
  public reorderStepToSave: IFunctionalFlowStep[] = [];

  public get allAlias() {
    return this.landscapeView.flows.map(f => f.alias);
  }

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeViewId) {
        vm.retrieveLandscapeView(to.params.landscapeViewId);
      }
    });
  }

  public retrieveLandscapeView(landscapeViewId) {
    let step: FunctionalFlowStep = new FunctionalFlowStep();
    step.description = 'EMPTYSTEP';
    let inter: FlowInterface = new FlowInterface();
    step.flowInterface = inter;
    if (this.emptySteps.length == 0) this.emptySteps.push(step);
    this.landscapeViewService()
      .find(landscapeViewId)
      .then(res => {
        console.log(res);
        this.landscapeView = res.landscape;
        this.consolidatedCapability = res.consolidatedCapability;
        if (this.landscapeView.compressedDrawSVG) {
          this.drawIoSVG = decodeURIComponent(escape(window.atob(this.landscapeView.compressedDrawSVG)));
        }
        this.getPlantUML(landscapeViewId);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getPlantUML(landscapeViewId) {
    this.refreshingPlantuml = true;
    this.landscapeViewService()
      .getPlantUML(landscapeViewId, this.layout, this.groupComponents)
      .then(
        res => {
          this.plantUMLImage = res.data;
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
    let xmlContent = 'data:text/xml;charset=utf-8,';
    xmlContent += this.landscapeView.compressedDrawXML;
    const data = encodeURI(xmlContent);
    const link = document.createElement('a');
    link.setAttribute('href', data);
    link.setAttribute('download', 'draw-io-export-' + this.landscapeView.diagramName.replace(/ /g, '-') + '.xml');
    link.click();
  }

  public prepareRemove(instance: ILandscapeView): void {
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

  // FLOW REORGANIZATION

  public startReorder() {
    this.reorderAlias = true;
    this.reorderAliasflowToSave = [];
    this.reorderStepToSave = [];
  }

  public reorder(step: IFunctionalFlowStep, functionalFlow: IFunctionalFlow, event) {
    const newFlowId: number = parseInt(event.target.value);
    const newFunctionalFlow: IFunctionalFlow = this.landscapeView.flows.find(f => f.id === newFlowId);

    // This is only for rendering page before save...
    // add interface in new Flow
    newFunctionalFlow.steps.push(step);
    // remove interface from old Flow
    functionalFlow.steps = functionalFlow.steps.filter(i => i.id != step.id);

    this.addStepToSave(newFunctionalFlow, step);

    // Add old & new Flows for later update by REST call
    // if (this.reorderAliasflowToSave.filter(e => e.id === functionalFlow.id).length === 0) {
    //   this.reorderAliasflowToSave.push(functionalFlow);
    // }
    // if (this.reorderAliasflowToSave.filter(e => e.id === newFunctionalFlow.id).length === 0) {
    //   this.reorderAliasflowToSave.push(newFunctionalFlow);
    // }

    this.reorderAllSteps(functionalFlow);
    this.reorderAllSteps(newFunctionalFlow);
  }

  public addStepToSave(newFunctionalFlow: IFunctionalFlow, step: IFunctionalFlowStep) {
    // remove if already exist to avoid duplicate
    // step.flow = newFunctionalFlow this cause an erro, for looping steps?
    let newFunctionalFlowSimplified: IFunctionalFlow = new FunctionalFlow();
    newFunctionalFlowSimplified = { ...newFunctionalFlow };
    newFunctionalFlowSimplified.steps = [];
    step.flow = newFunctionalFlowSimplified;
    this.reorderStepToSave.push(step);
  }

  public changeDescription(functionalFlow: IFunctionalFlow) {
    // Add old & new Flows for later update by REST call
    this.reorderAliasflowToSave = this.reorderAliasflowToSave.filter(e => e.id != functionalFlow.id);
    this.reorderAliasflowToSave.push(functionalFlow);
  }

  public changeStepDescription(functionalFlow: IFunctionalFlow, step: IFunctionalFlowStep) {
    // Add old & new Flows for later update by REST call
    this.reorderStepToSave = this.reorderStepToSave.filter(s => s.id != step.id);
    let newFunctionalFlowSimplified: IFunctionalFlow = new FunctionalFlow();
    newFunctionalFlowSimplified = { ...functionalFlow };
    newFunctionalFlowSimplified.steps = [];
    step.flow = newFunctionalFlowSimplified;
    this.reorderStepToSave.push(step);
  }

  public cancelReorder() {
    this.landscapeViewService()
      .find(this.landscapeView.id)
      .then(res => {
        this.landscapeView = res.landscape;
        this.consolidatedCapabilities = res.capabilities;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.reorderAlias = false;
    this.reorderAliasflowToSave = [];
  }

  public saveReorder() {
    let promises = [];
    this.reorderAliasflowToSave.forEach(flow => {
      promises.push(this.functionalFlowService().update(flow));
    });
    this.reorderStepToSave.forEach(step => {
      promises.push(this.functionalFlowStepService().update(step));
    });
    Promise.all(promises).then(res => {
      this.retrieveLandscapeView(this.landscapeView.id);
      this.reorderAlias = false;
      this.reorderAliasflowToSave = [];
    });
  }

  public swap(flow: IFunctionalFlow, i: number, j: number) {
    let tmp = flow.steps[i];
    flow.steps.splice(i, 1, flow.steps[j]);
    flow.steps.splice(j, 1, tmp);

    // reorder all steps in flow
    this.reorderAllSteps(flow);
  }

  public reorderAllSteps(flow: IFunctionalFlow) {
    flow.steps.forEach((step, i) => {
      if (step.stepOrder !== i + 1) {
        step.stepOrder = i + 1;
        this.addStepToSave(flow, step);
      }
    });
  }

  public exportExcel() {
    this.flowImportService()
      .downloadFile(this.landscapeView.id)
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'application/vnd.ms-excel',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', this.landscapeView.diagramName + '.xlsx');
        document.body.appendChild(link);
        link.click();
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
}
