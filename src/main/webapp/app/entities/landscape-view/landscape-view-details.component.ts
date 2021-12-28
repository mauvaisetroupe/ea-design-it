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

@Component
export default class LandscapeViewDetails extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public landscapeView: ILandscapeView = {};
  public plantUMLImage = '';

  public drawIoSVG = '';
  public isHidden = true;
  public isEditing = false;
  public drawIOToBeSaved = false;
  public emptyInterfaces = [new FlowInterface()];
  public functionalFlows: FunctionalFlow[] = [];
  public toBeSaved = false;
  public flowToDetach: number;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeViewId) {
        vm.retrieveLandscapeView(to.params.landscapeViewId);
      }
    });
  }

  public retrieveLandscapeView(landscapeViewId) {
    this.landscapeViewService()
      .find(landscapeViewId)
      .then(res => {
        this.landscapeView = res;
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
    this.landscapeViewService()
      .getPlantUML(landscapeViewId)
      .then(
        res => {
          this.plantUMLImage = res.data;
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
          this.functionalFlows = res.data;
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
      });
  }
}
