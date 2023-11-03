import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';
import DataObjectService from './data-object.service';
import { DataObjectImportDTO, type IDataObjectImportDTO } from '@/shared/model/data-objects-import-dto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataObjectImportUploadFile',
  setup() {
    const alertService = inject('alertService', () => useAlertService(), true);
    const dataObjectService = inject('dataObjectService', () => new DataObjectService());

    const route = useRoute();
    const router = useRouter();

    const dataObjectDTO: Ref<IDataObjectImportDTO> = ref(new DataObjectImportDTO());
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
        const res = await dataObjectService().uploadFile(excelFile.value.files[0]);
        dataObjectDTO.value = res.data;
        isFetching.value = false;
        rowsLoaded.value = true;
      } catch (error) {
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    }

    return {
      excelFile,
      excelFileName,
      isFetching,
      rowsLoaded,
      dataObjectDTO,
      handleFileUpload,
      submitFile,
      alertService,
    };
  },
});
