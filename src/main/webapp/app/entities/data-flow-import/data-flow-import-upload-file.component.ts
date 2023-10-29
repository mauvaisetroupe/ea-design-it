import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IDataFlowImport } from '@/shared/model/data-flow-import.model';
import DataFlowImportService from './data-flow-import.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFlowImportUploadFile',
  setup() {
    const dataFlowImportService = inject('dataFlowImportService', () => new DataFlowImportService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const dataFlowImports: Ref<IDataFlowImport[]> = ref([]);
    const dataFlowImportsNoFiltered: Ref<IDataFlowImport[]> = ref([]);
    const excelFile = ref();
    const isFetching = ref(false);
    const fileSubmited = ref(false);
    const rowsLoaded = ref(false);
    const excelFileName = ref('Browse File');

    function handleFileUpload(): void {
      //excelFile.value = event.target.files[0];
      excelFileName.value = excelFile.value.name;
    }

    function filterErrors() {
      dataFlowImports.value = [];
      dataFlowImportsNoFiltered.value.forEach(flowImport => {
        if (flowImport.importDataStatus === 'ERROR' || flowImport.importDataItemStatus === 'ERROR') {
          dataFlowImports.value.push(flowImport);
        }
      });
    }

    async function submitFile() {
      isFetching.value = true;
      fileSubmited.value = true;
      try {
        const res = await dataFlowImportService().uploadFile(excelFile.value.files[0]);
        dataFlowImports.value = res.data;
        dataFlowImportsNoFiltered.value = res.data;
        isFetching.value = false;
        rowsLoaded.value = true;
      } catch (error) {
        alertService().showHttpError(error.response);
      } finally {
        isFetching.value = false;
      }
    }

    function exportErrors() {
      const errors = getErrors();
      let csvContent = 'data:text/csv;charset=utf-8,';
      csvContent += [Object.keys(errors[0]).join(';'), ...errors.map(row => Object.values(row).join(';').replace(/\n/gm, ''))]
        .join('\n')
        .replace(/(^\[)|(\]$)/gm, '');
      const data = encodeURI(csvContent);
      const link = document.createElement('a');
      link.setAttribute('href', data);
      link.setAttribute('download', 'export.csv');
      link.click();
    }

    function getErrors() {
      const errors: IDataFlowImport[] = [];
      dataFlowImportsNoFiltered.value.forEach(flowImport => {
        if (flowImport.importDataStatus === 'ERROR' || flowImport.importDataItemStatus === 'ERROR') {
          const errorRow = {
            ...flowImport,
          };
          errors.push(errorRow);
          console.log(errorRow);
        }
      });
      return errors;
    }

    return {
      excelFile,
      excelFileName,
      isFetching,
      rowsLoaded,
      dataFlowImportService,
      filterErrors,
      handleFileUpload,
      submitFile,
      exportErrors,
    };
  },
});
