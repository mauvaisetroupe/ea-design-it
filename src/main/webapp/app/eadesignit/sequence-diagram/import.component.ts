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

  public getPlantUML() {
    this.sequenceDiagramService()
      .getPlantUML(this.plantuml)
      .then(
        res => {
          this.plantUMLImage = res.data;
          this.isFetching = false;
        },
        err => {
          console.log(err);
        }
      );
  }

  public importPlantUML() {
    this.sequenceDiagramService()
      .importPlantuml(this.plantuml)
      .then(
        res => {
          this.functionalFlow = res.data;
          this.isFetching = false;
        },
        err => {
          console.log(err);
        }
      );
  }
}
