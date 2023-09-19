import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import SequenceDiagramService from './import.service';
import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import AlertService from '@/shared/alert/alert.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

@Component
export default class SequenceDiagram extends Vue {
  @Inject('sequenceDiagramService') private sequenceDiagramService: () => SequenceDiagramService;
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('functionalFlowService') private functionalFlowService: () => FunctionalFlowService;

  public plantuml = '';
  public plantUMLImage = '';
  public isFetching = false;
  public functionalFlow = null;
  public importError = '';
  public previewError = '';

  public existingLandscapes: ILandscapeView[] = null;
  public selectedLandscape = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.query.functionalFlowId) {
        vm.retrieveFunctionalFlow(to.query.functionalFlowId);
      }
    });
  }

  public mounted(): void {
    this.retrieveAllLandscapeViews();
  }

  public retrieveAllLandscapeViews(): void {
    this.isFetching = true;
    this.landscapeViewService()
      .retrieve()
      .then(
        res => {
          this.existingLandscapes = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  ////////////////////////////////////////////////
  // On load, retrieve
  // - FunctionFlow,
  // - plantUML source fron flowID
  // - plantUML image
  // - table with potential interfaces (should be the same than flow detail)
  /////////////////////////////////////////////////

  // STEP 1 - Retrieve FunctionalFlow
  public retrieveFunctionalFlow(functionalFlowId) {
    this.functionalFlowService()
      .find(functionalFlowId)
      .then(res => {
        this.functionalFlow = res;
        this.getPlantUMLSourceFromFlowId(functionalFlowId);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  // STEP 2 - Retrieve plantuml source from flow ID
  public getPlantUMLSourceFromFlowId(functionalFlowId) {
    this.functionalFlowService()
      .getPlantUMLSource(functionalFlowId, true, true)
      .then(
        res => {
          this.plantuml = res.data;
          this.isFetching = false;
          this.getPlantUMLImageFromString();
        },
        err => {
          console.log(err);
        }
      );
  }

  // STEP 3 : Retrieve plantuml Image from plantuml source
  public getPlantUMLImageFromString() {
    this.sequenceDiagramService()
      .getPlantUMLFromString(this.plantuml)
      .then(
        res => {
          this.plantUMLImage = res.data;
          this.isFetching = false;
          this.previewError = '';
          this.importPlantuml();
        },
        err => {
          console.log(err);
          this.plantUMLImage = '';
          this.functionalFlow = null;
          this.previewError = err;
        }
      );
  }

  // STEP 3 : Retrieve interface list from plantuml Source
  public importPlantuml() {
    this.sequenceDiagramService()
      .importPlantuml(this.plantuml)
      .then(
        res => {
          this.functionalFlow = res.data;
          this.isFetching = false;
          this.importError = '';
        },
        err => {
          this.plantUMLImage = '';
          this.functionalFlow = '';
          this.importError = err;
        }
      );
  }

  public saveImport() {
    // this.getPlantUML();
    // this.sequenceDiagramService()
    //   .saveImport(this.functionalFlow, this.selectedLandscape)
    //   .then(
    //     res => {
    //       this.$router.push({ name: 'FunctionalFlowView', params: { functionalFlowId: res.data.id } });
    //     },
    //     err => {
    //       this.plantUMLImage = '';
    //       this.functionalFlow = '';
    //       this.importError = err;
    //     }
    //   );
  }

  public changeInterface(flowimportLine) {
    if (flowimportLine.selectedInterface && flowimportLine.selectedInterface.protocol) {
      flowimportLine.protocol = flowimportLine.selectedInterface.protocol;
    }
  }
}
