import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import CapabilityImportService from './capability-import.service';
import {
  type IApplicationCapabilityImport,
  type IApplicationCapabilityImportItem,
} from '@/shared/model/application-capability-import.model';
import { type ISummary } from '@/shared/model/summary-sheet.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CApplicationCapabilityImport',
  setup() {
    const capabilityImportService = inject('capabilityImportService', () => new CapabilityImportService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const dtos: Ref<IApplicationCapabilityImport[]> = ref([]);
    const notFilteredDtos: Ref<IApplicationCapabilityImport[]> = ref([]);
    const excelFile = ref();
    const isFetching = ref(false);
    const fileSubmited = ref(false);
    const analyzeDone = ref(false);
    const rowsLoaded = ref(false);
    const excelFileName = ref('Browse File');

    const checkedNames: Ref<string[]> = ref([]);
    const summary: Ref<ISummary[]> = ref([]);

    // STEP 1 - Upload file and retreive all sheet with name starting with FLW

    function handleFileUpload(): void {
      excelFileName.value = excelFile.value.files[0].name;
    }

    async function getSheetnames() {
      isFetching.value = true;
      try {
        const res = await capabilityImportService().getSummary(excelFile.value.files[0]);
        summary.value = res.filter(sum => sum).filter(sum => sum.entityType === 'Capability Mapping');
        checkedNames.value = summary.value.map(sum => sum.sheetName);
        analyzeDone.value = true;
      } catch (error) {
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    }

    function selectAll() {
      if (!fileSubmited.value) {
        checkedNames.value = [];
        checkedNames.value.push(...summary.value.map(sum => sum.sheetName));
      }
    }

    function selectNone() {
      if (!fileSubmited.value) checkedNames.value = [];
    }

    // Step 2 - Submit de file and selected sheet names

    function submitFile(): void {
      isFetching.value = true;
      fileSubmited.value = true;
      // send file n times, sheet by sheet
      // this is not optimal, but it's the easier way to have a reactive behavior and avoid time out
      // serialized to avoid database transactional problem
      uploadOneSheet();
    }

    async function uploadOneSheet() {
      const sheetToProcess: string | undefined = checkedNames.value.shift();
      const sheetToProcessArray = sheetToProcess ? [sheetToProcess] : [];
      try {
        const res = await capabilityImportService().uploadMappingFile(excelFile.value.files[0], sheetToProcessArray);
        dtos.value.push(...res.data);
        notFilteredDtos.value.push(...res.data);
        rowsLoaded.value = true;
        if (checkedNames.value.length > 0) {
          uploadOneSheet();
        } else {
          isFetching.value = false;
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
        const newitems: IApplicationCapabilityImportItem[] = [];
        dto.dtos.forEach(item => {
          if (item.importStatus === 'ERROR') {
            newitems.push(item);
          }
        });
        let newdto: IApplicationCapabilityImport = {};
        newdto.dtos = newitems;
        newdto.sheetname = dto.sheetname;
        dtos.value.push(newdto);
      });
    }

    return {
      excelFile,
      excelFileName,
      isFetching,
      rowsLoaded,
      dtos,
      notFilteredDtos,
      summary,
      checkedNames,
      fileSubmited,
      analyzeDone,
      filterErrors,
      handleFileUpload,
      getSheetnames,
      selectAll,
      selectNone,
      submitFile,
    };
  },
});
