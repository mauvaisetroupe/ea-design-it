import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import SequenceDiagramService from './import.service';

@Component
export default class SequenceDiagram extends Vue {
  @Inject('sequenceDiagramService') private sequenceDiagramService: () => SequenceDiagramService;
  public plantuml = '';
  public plantUMLImage = '';
  public isFetching = false;
  public functionalFlow = {};
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
          this.functionalFlow = '';
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
}
