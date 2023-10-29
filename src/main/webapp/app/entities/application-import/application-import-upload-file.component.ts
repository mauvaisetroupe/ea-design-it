import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IApplicationImport } from '@/shared/model/application-import.model';
import ApplicationImportService from './application-import.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationImportUploadFile',
  setup() {
    const applicationImportService = inject('applicationImportService', () => new ApplicationImportService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const applicationImports: Ref<IApplicationImport[]> = ref([]);
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
        const res = await applicationImportService().uploadFile(excelFile.value.files[0]);
        applicationImports.value = res.data;
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
      applicationImports,
      handleFileUpload,
      submitFile,
    };
  },
});
