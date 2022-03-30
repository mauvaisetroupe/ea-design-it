import { Component, Vue, Inject } from 'vue-property-decorator';

import applicationsDiagramService from './applications-diagram.service';
import { IFlowInterface } from '@/shared/model/flow-interface.model';

@Component
export default class ApplicationsDiagram extends Vue {
  @Inject('applicationsDiagramService') private applicationsDiagramService: () => applicationsDiagramService;
  public interfaces: IFlowInterface[] = [];
  public sequenceDiagram: boolean = true;
  public plantUMLImage = '';
  public applicationIds = [];

  beforeRouteEnter(to, from, next) {
    next(vm => {
      console.log(to.query.id);
      vm.generateDiagramForSelection(to.query.id);
    });
  }

  public generateDiagramForSelection(applicationIds: number[]) {
    this.applicationIds = applicationIds;
    this.applicationsDiagramService()
      .createNewFromApplications(applicationIds)
      .then(res => {
        this.interfaces = res;
        this.getPlantUMLforapplications(applicationIds);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }

  public exportPlantUML() {
    this.applicationsDiagramService()
      .getPlantUMSourceforApplications(this.applicationIds)
      .then(response => {
        const url = URL.createObjectURL(
          new Blob([response.data], {
            type: 'text/plain',
          })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'plantuml.txt');
        document.body.appendChild(link);
        link.click();
      });
  }

  public getPlantUMLforapplications(aplicationIds: number[]) {
    console.log('Entering in method getPlantUMLforapplications');
    this.applicationsDiagramService()
      .getPlantUMLforApplications(aplicationIds)
      .then(
        res => {
          console.log(res.data);

          this.plantUMLImage = res.data;
        },
        err => {
          console.log(err);
        }
      );
  }
}
