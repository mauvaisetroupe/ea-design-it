import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import CapabilityImportService from './capability-import.service';
import { type ICapabilityImport } from '@/shared/model/capability-import.model';
import { type ICapabilityActionDTO, type ICapabilityImportAnalysisDTO } from '@/shared/model/capability-import-analysis.model';
import { Action } from '@/shared/model/capability-import-analysis.model';
import { type ICapability } from '@/shared/model/capability.model';
import CapabilityTreeComponent from '@/entities/capability/component/capability-tree.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityImport',
  components: { CapabilityTreeComponent },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const accountService = inject<AccountService>('accountService');
    const alertService = inject('alertService', () => useAlertService(), true);
    const capabilityImportService = inject('capabilityImportService', () => new CapabilityImportService());

    const capabilitiesImports: Ref<ICapabilityImport[]> = ref([]);
    const filteredCapabilitiesImports: Ref<ICapabilityImport[]> = ref([]);
    const excelFile = ref();
    const isFetching = ref(false);
    const fileSubmited = ref(false);
    const rowsLoaded = ref(false);
    const excelFileName = ref('Browse File');
    const analysisDone = ref(false);
    const capabilitiesImportAnalysis: Ref<ICapabilityImportAnalysisDTO> = ref({});

    const toAddOption = [
      { text: 'Import', value: Action.ADD },
      { text: 'Ignore', value: Action.IGNORE },
    ];
    const toDeleteOption = [
      { text: 'Delete', value: Action.DELETE },
      { text: 'Ignore', value: Action.IGNORE },
    ];
    const toDeleteWithMappingOption = [
      { text: 'Force Delete', value: Action.FORCE_DELETE },
      { text: 'Ignore', value: Action.IGNORE },
    ];
    const toDeleteWithChildMappingOption = [{ text: 'Ignore', value: Action.IGNORE }];
    const IGNORE = Action.IGNORE;
    const DELETE = Action.DELETE;
    const FORCE_DELETE = Action.FORCE_DELETE;
    const ADD = Action.ADD;

    function handleFileUpload(): void {
      excelFileName.value = excelFile.value.files[0].name;
    }

    async function submitFileForAnalysis() {
      isFetching.value = true;
      fileSubmited.value = true;
      capabilitiesImports.value = [];
      filteredCapabilitiesImports.value = [];
      try {
        const res = await capabilityImportService().uploadFileToAnalysis(excelFile.value.files[0]);
        capabilitiesImportAnalysis.value = res;
        analysisDone.value = true;
        isFetching.value = false;
      } catch (error) {
        alertService.showAnyError(error);
        analysisDone.value = false;
      } finally {
        isFetching.value = false;
      }
    }

    async function confirmUploadedFile() {
      isFetching.value = true;
      fileSubmited.value = true;
      capabilitiesImports.value = [];
      filteredCapabilitiesImports.value = [];
      try {
        const res = await capabilityImportService().confirmUploadedFile(capabilitiesImportAnalysis.value);
        analysisDone.value = false;
        capabilitiesImports.value = res.data;
        filteredCapabilitiesImports.value = res.data;
        rowsLoaded.value = true;
      } catch (error) {
        alertService.showAnyError(error);
      } finally {
        isFetching.value = false;
      }
    }

    // Error Handling

    const somethingToImport: Ref<boolean> = computed(() => {
      return (
        analysisDone.value &&
        !(
          !capabilitiesImportAnalysis.value?.capabilitiesToAdd?.length &&
          !capabilitiesImportAnalysis.value?.capabilitiesToDelete?.length &&
          !capabilitiesImportAnalysis.value?.capabilitiesToDeleteWithMappings?.length &&
          !capabilitiesImportAnalysis.value?.ancestorsOfCapabilitiesWithMappings?.length
        )
      );
    });

    function filterErrors() {
      filteredCapabilitiesImports.value = filteredCapabilitiesImports.value.filter(c => c.status === 'ERROR');
    }

    function toggleAll(capabilityActionss: ICapabilityActionDTO[], action: Action) {
      capabilityActionss.forEach(capaAction => {
        capaAction.action = action;
      });
    }

    return {
      excelFile,
      excelFileName,
      isFetching,
      rowsLoaded,
      somethingToImport,
      analysisDone,
      capabilitiesImportAnalysis,
      toAddOption,
      toDeleteOption,
      toDeleteWithMappingOption,
      toDeleteWithChildMappingOption,
      capabilitiesImports,
      filteredCapabilitiesImports,
      IGNORE,
      DELETE,
      FORCE_DELETE,
      ADD,
      handleFileUpload,
      submitFileForAnalysis,
      confirmUploadedFile,
      filterErrors,
      toggleAll,
    };
  },
});
