import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import FullExportService from './full-export.service';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import type { ILandscapeView } from '@/shared/model/landscape-view.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FullExport',
  setup() {
    const fullExportService = inject('fullExportService', () => new FullExportService());
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const isFetching = ref(false);
    const applications = ref(true);
    const applicationComponents = ref(true);
    const landscapes: Ref<ILandscapeView[]> = ref([]);
    const checkedLandscapes: Ref<ILandscapeView[]> = ref([]);
    const owner = ref(true);
    const externalSystem = ref(true);
    const capabilities = ref(true);
    const capabilitiesMapping: Ref<ILandscapeView[]> = ref([]);
    const checkedCapabilitiesMapping: Ref<ILandscapeView[]> = ref([]);
    const capabilitiesMappingWithNoLandscape = ref(true);
    const functionalFlowsWhithNoLandscape = ref(true);
    const submited = ref(false);

    onMounted(async () => {
      await retrieveAllLandscapeViews();
    });

    const retrieveLandscapeViews = async () => {
      isFetching.value = true;
      try {
        const res = await landscapeViewService().retrieve();
        landscapes.value = res.data;
        checkedLandscapes.value = res.data;
        capabilitiesMapping.value = res.data;
        checkedCapabilitiesMapping.value = res.data;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    function selectAllLansdcape() {
      if (!submited.value) {
        checkedLandscapes.value = [];
        checkedLandscapes.value.push(...landscapes.value);
      }
    }

    function selectNoLandscape() {
      if (!submited.value) checkedLandscapes.value = [];
    }

    function selectAllMapping() {
      if (!submited.value) {
        checkedCapabilitiesMapping.value = [];
        checkedCapabilitiesMapping.value.push(...capabilitiesMapping.value);
        capabilities.value = true;
      }
    }

    function selectNoMapping() {
      if (!submited.value) checkedCapabilitiesMapping.value = [];
    }

    function checkCapa(e) {
      if (checkedCapabilitiesMapping.value.length > 0) {
        capabilities.value = true;
      }
    }

    function exportExcel() {
      submited.value = true;
      isFetching.value = true;
      fullExportService()
        .downloadFile(
          applications.value,
          applicationComponents.value,
          owner.value,
          externalSystem.value,
          capabilities.value,
          checkedLandscapes.value.map(l => l.id),
          checkedCapabilitiesMapping.value.map(l => l.id),
          capabilitiesMappingWithNoLandscape.value,
          functionalFlowsWhithNoLandscape.value,
        )
        .then(
          response => {
            const url = URL.createObjectURL(
              new Blob([response.data], {
                type: 'application/vnd.ms-excel',
              }),
            );
            const link = document.createElement('a');
            link.href = url;
            const today = new Date().toISOString().split('T')[0];
            const time = new Date().toLocaleTimeString().replace(' ', '_');
            link.setAttribute('download', 'full-data-export-' + today + '-' + time + '.xlsx');
            document.body.appendChild(link);
            link.click();
            isFetching.value = false;
          },
          err => {
            isFetching.value = false;
            alertService().showHttpError(err.response);
          },
        );
    }

    return {
      isFetching,
      applications,
      applicationComponents,
      landscapes,
      checkedLandscapes,
      owner,
      externalSystem,
      capabilities,
      capabilitiesMapping,
      checkedCapabilitiesMapping,
      capabilitiesMappingWithNoLandscape,
      functionalFlowsWhithNoLandscape,
      submited,
      selectAllLansdcape,
      selectNoLandscape,
      selectAllMapping,
      selectNoMapping,
      checkCapa,
      exportExcel,
    };
  },
});
