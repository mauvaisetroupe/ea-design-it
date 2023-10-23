import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IExternalSystem } from '@/shared/model/external-system.model';
import ExternalSystemService from '@/entities/external-system/external-system.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalSystemUploadFile',
  setup() {
    const externalSystemService = inject('externalSystemService', () => new ExternalSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const externalSystems: Ref<IExternalSystem[]> = ref([]);
    const excelFile = ref();
    const isFetching = ref(false);
    const fileSubmited = ref(false);
    const rowsLoaded = ref(false);
    const excelFileName = ref('Browse File');

    function handleFileUpload(): void {
      excelFileName.value = excelFile.value.files[0].name;
    }

    async function submitFile() {
      isFetching.value = true;
      fileSubmited.value = true;
      try {
        const res = await externalSystemService().uploadFile(excelFile.value.files[0]);
        externalSystems.value = res.data;
        isFetching.value = false;
        rowsLoaded.value = true;
      } catch (error) {
        alertService().showHttpError(error.response);
      } finally {
        isFetching.value = false;
      }
    }

    return {
      excelFile,
      excelFileName,
      isFetching,
      rowsLoaded,
      externalSystemService,
      handleFileUpload,
      submitFile,
      externalSystems,
    };
  },
});
