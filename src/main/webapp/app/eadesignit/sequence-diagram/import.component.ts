import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import SequenceDiagramService from './import.service';

@Component
export default class SequenceDiagram extends Vue {
  @Inject('sequenceDiagramService') private sequenceDiagramService: () => SequenceDiagramService;
  public plantuml = '';
  public plantUMLImage = '';
  public isFetching = false;
  public functionalFlow = null;
  public importError = '';
  public previewError = '';

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
      .saveImport(this.functionalFlow)
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
