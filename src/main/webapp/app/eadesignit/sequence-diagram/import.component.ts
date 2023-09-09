import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import SequenceDiagramService from './import.service';
import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class SequenceDiagram extends Vue {
  @Inject('sequenceDiagramService') private sequenceDiagramService: () => SequenceDiagramService;
  @Inject('landscapeViewService') private landscapeViewService: () => LandscapeViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public plantuml = '';
  public plantUMLImage = '';
  public isFetching = false;
  public functionalFlow = null;
  public importError = '';
  public previewError = '';

  public existingLandscapes: ILandscapeView[] = null;
  public selectedLandscape = '';

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

  public getPlantUML() {
    this.sequenceDiagramService()
      .getPlantUML(this.plantuml)
      .then(
        res => {
          this.plantUMLImage = res.data;
          this.isFetching = false;
          this.previewError = '';
        },
        err => {
          console.log(err);
          this.plantUMLImage = '';
          this.functionalFlow = null;
          this.previewError = err;
        }
      );
  }

  public importPlantUML() {
    this.getPlantUML();

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
    this.getPlantUML();
    this.sequenceDiagramService()
      .saveImport(this.functionalFlow, this.selectedLandscape)
      .then(
        res => {
          this.$router.push({ name: 'FunctionalFlowView', params: { functionalFlowId: res.data.id } });
        },
        err => {
          this.plantUMLImage = '';
          this.functionalFlow = '';
          this.importError = err;
        }
      );
  }

  public changeInterface(flowimportLine) {
    if (flowimportLine.selectedInterface && flowimportLine.selectedInterface.protocol) {
      flowimportLine.protocol = flowimportLine.selectedInterface.protocol;
    }
  }
}
