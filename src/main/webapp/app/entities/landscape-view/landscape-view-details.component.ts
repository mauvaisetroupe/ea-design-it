import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ILandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';
import { FlowImport, IFlowImport } from '@/shared/model/flow-import.model';

@Component
export default class LandscapeViewDetails extends mixins(JhiDataUtils) {
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeView: ILandscapeView = {};
  public plantUMLImage = '';
  public captions = [];

  public drawIoSVG = '';
  public isHidden = true;
  public isEditing = false;

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
        this.fillCaption();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
    this.getPlantUML(landscapeViewId);

    if (this.landscapeView.compressedDrawSVG) {
      this.drawIoSVG = btoa(this.landscapeView.compressedDrawSVG);
    }
  }

  public fillCaption() {
    this.landscapeView.flows.forEach(flow => {
      var firstLine = true;
      flow.interfaces.forEach(inter => {
        if (firstLine) {
          var xxx = flow.alias;
        } else {
          xxx = '|';
        }
        var caption = {
          flowAlias: xxx,
          flowID: flow.id,
          interfaceAlias: inter.alias,
          interfaceID: inter.id,
          description: flow.description,
          protocol: inter.protocol,
          source: inter.source.name,
          target: inter.target.name,
        };
        this.captions.push(caption);
        firstLine = false;
      });
    });
  }

  public previousState() {
    this.$router.go(-1);
  }

  public getPlantUML(landscapeViewId) {
    console.log('Entering in method getPlantUML');
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
    console.log('PROUT');
    this.isHidden = false;
    this.isEditing = true;
  }

  public receiveMessage(evt) {
    var iframe: HTMLIFrameElement = document.getElementById('myDiv') as HTMLIFrameElement;
    //console.log(evt)
    if (evt.data.length > 0) {
      console.log(evt.data);
      var msg = JSON.parse(evt.data);
      console.log(msg);

      if (msg.event == 'init') {
        console.log('slaut gros gros');
        console.log(iframe);
        //iframe.contentWindow.postMessage(JSON.stringify({action: 'load',autosave: 1, xml: '<mxGraphModel dx="1350" dy="793" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="850" pageHeight="1100" math="0" shadow="0"><root><mxCell id="0" /><mxCell id="1" parent="0" /><mxCell id="vISkdTzWN7_PVdaD8Uo9-1" value="" style="ellipse;whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="310" y="170" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-2" value="TEST" style="whiteSpace=wrap;html=1;aspect=fixed;" parent="1" vertex="1"><mxGeometry x="140" y="250" width="80" height="80" as="geometry" /></mxCell><mxCell id="vISkdTzWN7_PVdaD8Uo9-3" value="" style="endArrow=classic;html=1;rounded=0;exitX=1;exitY=0.5;exitDx=0;exitDy=0;entryX=0;entryY=1;entryDx=0;entryDy=0;" parent="1" source="vISkdTzWN7_PVdaD8Uo9-2" target="vISkdTzWN7_PVdaD8Uo9-1" edge="1"><mxGeometry width="50" height="50" relative="1" as="geometry"><mxPoint x="400" y="430" as="sourcePoint" /><mxPoint x="450" y="380" as="targetPoint" /></mxGeometry></mxCell></root></mxGraphModel>'}), '*');
        iframe.contentWindow.postMessage(JSON.stringify({ action: 'load', autosave: 1, xml: this.landscapeView.compressedDrawXML }), '*');
      } else if (msg.event == 'export') {
        // Extracts SVG DOM from data URI to enable links
        var svg = atob(msg.data.substring(msg.data.indexOf(',') + 1));
        console.log(svg);
        this.drawIoSVG = svg;
        this.isHidden = true;
        this.isEditing = false;
      } else if (msg.event == 'save') {
        iframe.contentWindow.postMessage(JSON.stringify({ action: 'export', format: 'xmlsvg', xml: msg.xml, spin: 'Updating page' }), '*');
        console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>');
        console.log(msg.xml);
        this.landscapeView.compressedDrawXML = msg.xml;
      } else if (msg.event == 'exit') {
        this.isHidden = true;
        this.isEditing = false;
      }
    }
  }
}
