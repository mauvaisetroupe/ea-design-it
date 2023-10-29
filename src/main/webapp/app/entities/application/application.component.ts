import { computed, defineComponent, inject, onMounted, ref, type Ref } from 'vue';

import ApplicationService from './application.service';
import { type IApplication } from '@/shared/model/application.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';
import ApplicationImportService from '@/entities/application-import/application-import.service';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';
import { useStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Application',
  setup() {
    const applicationService = inject('applicationService', () => new ApplicationService());
    const applicationImportService = inject('applicationImportService', () => new ApplicationImportService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject<AccountService>('accountService');

    const applications: Ref<IApplication[]> = ref([]);
    const selectedApplicationIds: Ref<number[]> = ref([]);

    const isFetching = ref(false);
    const filter = ref('');
    const showAdvanced = ref(false);

    const sortBy = 'name';
    const sortDesc = false;
    const filterOn = ref(['alias', 'name']);
    const fields = [
      { key: 'CHECKBOX', sortable: false, label: '' },
      // { key: 'id', sortable: false },
      { key: 'alias', sortable: true },
      { key: 'name', sortable: true },
      { key: 'description', sortable: false, formatter: formatLongText },
      // {key:'comment', sortable: false},
      // {key:'documentationURL', sortable: false},
      // {key:'startDate', sortable: false},
      // {key:'endDate', sortable: false},
      { key: 'applicationType', sortable: false },
      { key: 'softwareType', sortable: false },
      { key: 'nickname', sortable: false },
      { key: 'owner.name', sortable: false, label: 'Owner' },
      { key: 'itOwner.name', sortable: false, label: 'IT Owner' },
      { key: 'businessOwner.name', sortable: false, label: 'Business Owner' },
      { key: 'categories', sortable: false },
      { key: 'technologies', sortable: false },
      //{ key: 'capabilities', sortable: false },
      // {key:'externalIDS', sortable: false},
      // {key:'applicationsLists', sortable: false},
    ];
    const totalRows = ref(0);
    const currentPage = ref(1);
    const applicationTypeValues: Ref<string[]> = ref(Object.keys(ApplicationType));
    const softwareTypeValues: Ref<string[]> = ref(Object.keys(SoftwareType));
    const perPage = computed(() => {
      return filter.value ? 100 : 7;
    });

    const applicationTypeSelected = ref('');
    const softwareTypeSelected = ref('');

    function formatLongText(value, key, item) {
      const max = 600;
      if (value && value.length > max) {
        return value.substring(0, max) + '...';
      } else {
        return value;
      }
    }

    function onFiltered(filteredItems) {
      // Trigger pagination to update the number of buttons/pages due to filtering
      totalRows.value = filteredItems?.length;
      currentPage.value = 1;
    }

    const filteredApplications: Ref<IApplication[]> = computed(() => {
      return applications.value.filter(app => {
        let appType = true;
        if (applicationTypeSelected.value) {
          appType = app.applicationType && app.applicationType.valueOf() == applicationTypeSelected.value;
        }
        let softwareType = true;
        if (softwareTypeSelected.value) {
          softwareType = app.softwareType && app.softwareType.valueOf() == softwareTypeSelected.value;
        }
        return appType && softwareType;
      });
    });

    const clear = () => {};

    const retrieveApplications = async () => {
      isFetching.value = true;
      try {
        const res = await applicationService().retrieve();
        applications.value = res.data;
        totalRows.value = applications.value.length;
      } catch (err) {
        alertService.showAnyError(err);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      sessionStorage.removeItem('applications');
      retrieveApplications();
    };

    onMounted(async () => {
      await retrieveApplications();
    });

    function exportExcel() {
      applicationImportService()
        .downloadFile()
        .then(response => {
          const url = URL.createObjectURL(
            new Blob([response.data], {
              type: 'application/vnd.ms-excel',
            }),
          );
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'applications-export.xlsx');
          document.body.appendChild(link);
          link.click();
        });
    }

    return {
      applications,
      handleSyncList,
      isFetching,
      retrieveApplications,
      clear,
      filterOn,
      filteredApplications,
      accountService,
      selectedApplicationIds,
      currentPage,
      totalRows,
      perPage,
      showAdvanced,
      filter,
      applicationTypeSelected,
      softwareTypeSelected,
      softwareTypeValues,
      applicationTypeValues,
      onFiltered,
      fields,
      sortBy,
      sortDesc,
      exportExcel,
    };
  },
});
