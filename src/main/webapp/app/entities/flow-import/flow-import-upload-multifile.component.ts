import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IFlowImport } from '@/shared/model/flow-import.model';
import FlowImportService from './flow-import.service';
import AlertService from '@/shared/alert/alert.service';
import { type ISummary } from '@/shared/model/summary-sheet.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowImportUploadMultiFile',
  setup() {
    const route = useRoute();
    const router = useRouter();

    const dtos: Ref<[]> = ref([]);
    const notFilteredDtos: Ref<[]> = ref([]);
    const alertService = inject('alertService', () => useAlertService(), true);
    const flowImportService = inject('flowImportService', () => new FlowImportService());
    const excelFile = ref();
    const isFetching = ref(false);
    const fileToBeSubmited = ref(false);
    const fileSubmited = ref(false);
    const analyseToBeImported = ref(false);
    const rowsLoaded = ref(false);
    const excelFileName = ref('Browse File');

    const checkedNames: Ref<string[]> = ref([]);
    const summary: Ref<ISummary[]> = ref([]);

    function handleFileUpload(): void {
      excelFileName.value = excelFile.value.files[0].name;
      fileToBeSubmited.value = true;
    }

    // STEP 1 - Upload file and retreive all sheet with name starting with FLW

    async function getSheetnames() {
      isFetching.value = true;
      fileSubmited.value = true;
      try {
        const res = await flowImportService().getSummary(excelFile.value.files[0]);
        console.log(res);
        summary.value = res.filter(sum => sum).filter(sum => sum.entityType === 'Landscape');
        checkedNames.value = summary.value.map(sum => sum.sheetName);
      } catch (error) {
        console.error(error);
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    }

    function selectAll() {
      checkedNames.value = [];
      checkedNames.value.push(...summary.value.map(sum => sum.sheetName));
    }

    function selectNone() {
      checkedNames.value = [];
    }

    // Step 2 - Submit de file and selected sheet names

    function submitFile(): void {
      isFetching.value = true;
      analyseToBeImported.value = true;
      // send file n times, sheet by sheet
      // this is not optimal, but it's the easier way to have a reactive behavior and avoid time out
      // serialized to avoid database transactional problem
      uploadOneSheet();
    }

    async function uploadOneSheet() {
      const sheetToProcess: string | undefined = checkedNames.value.shift();
      const sheetToProcessArray = sheetToProcess ? [sheetToProcess] : [];
      try {
        const res = await flowImportService().uploadMultipleFile(excelFile.value.files[0], sheetToProcessArray);

        dtos.value.push(...res.data);
        notFilteredDtos.value.push(...res.data);
        rowsLoaded.value = true;
        if (checkedNames.value.length > 0) {
          uploadOneSheet();
        }
      } catch (error) {
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    }

    // step 3 - Give possibility to filer errors

    function filterErrors() {
      dtos.value = [];
      notFilteredDtos.value.forEach(dto => {
        let newdto = {};
        const newimport = [];
        dto.flowImports.forEach(elem => {
          const flowImport = elem as IFlowImport;
          if (
            flowImport.importFunctionalFlowStatus === 'ERROR' ||
            flowImport.importInterfaceStatus === 'ERROR' ||
            flowImport.importDataFlowStatus === 'ERROR'
          ) {
            newimport.push(flowImport);
          }
        });
        newdto = {
          excelFileName: dto.excelFileName,
          flowImports: newimport,
        };
        dtos.value.push(newdto);
      });
    }

    return {
      excelFile,
      isFetching,
      rowsLoaded,
      handleFileUpload,
      submitFile,
      summary,
      getSheetnames,
      selectAll,
      selectNone,
      checkedNames,
      fileSubmited,
      dtos,
      excelFileName,
      filterErrors,
      fileToBeSubmited,
      analyseToBeImported,
    };
  },
});
