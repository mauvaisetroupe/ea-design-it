import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ApplicationsDiagramService from './applications-diagram.service';
import { type IFlowInterface } from '@/shared/model/flow-interface.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationsDiagram',
  setup() {
    const applicationsDiagramService = inject('applicationsDiagramService', () => new ApplicationsDiagramService());

    const route = useRoute();
    const router = useRouter();

    const interfaces: Ref<IFlowInterface[]> = ref([]);
    const sequenceDiagram: Ref<boolean> = ref(true);
    const plantUMLImage: Ref<string> = ref('');
    const applicationIds: Ref<number[]> = ref([]);

    if (route.query?.id) {
      console.log(route.query.id);
      generateDiagramForSelection(route.query.id);
    }

    function generateDiagramForSelection(applicationIds) {
      applicationsDiagramService()
        .createNewFromApplications(applicationIds)
        .then(res => {
          interfaces.value = res;
          getPlantUMLforapplications(applicationIds);
        });
    }

    const previousState = () => router.go(-1);

    function exportPlantUML() {
      applicationsDiagramService()
        .getPlantUMSourceforApplications(applicationIds.value)
        .then(response => {
          const url = URL.createObjectURL(
            new Blob([response.data], {
              type: 'text/plain',
            }),
          );
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'plantuml.txt');
          document.body.appendChild(link);
          link.click();
        });
    }

    function getPlantUMLforapplications(aplicationIds: number[]) {
      console.log('Entering in method getPlantUMLforapplications');
      applicationsDiagramService()
        .getPlantUMLforApplications(aplicationIds)
        .then(
          res => {
            console.log(res.data);
            plantUMLImage.value = res.data;
          },
          err => {
            console.log(err);
          },
        );
    }

    return {
      plantUMLImage,
      exportPlantUML,
      previousState,
      interfaces,
    };
  },
});
