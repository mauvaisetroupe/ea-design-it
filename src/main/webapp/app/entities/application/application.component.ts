import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';

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

    const clear = () => {};

    const retrieveApplications = async () => {
      isFetching.value = true;
      try {
        const res = await applicationService().retrieve();
        applications.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApplications();
    };

    onMounted(async () => {
      await retrieveApplications();
    });

    //const removeId: Ref<number> = ref(null);
    // const removeEntity = ref<any>(null);
    // const prepareRemove = (instance: IApplication) => {
    //   removeId.value = instance.id;
    //   removeEntity.value.show();
    // };
    // const closeDialog = () => {
    //   removeEntity.value.hide();
    // };
    // const removeApplication = async () => {
    //   try {
    //     await applicationService().delete(removeId.value);
    //     const message = 'A Application is deleted with identifier ' + removeId.value;
    //     alertService.showInfo(message, { variant: 'danger' });
    //     removeId.value = null;
    //     retrieveApplications();
    //     closeDialog();
    //   } catch (error) {
    //     alertService.showHttpError(error.response);
    //   }
    // };

    return {
      applications,
      handleSyncList,
      isFetching,
      retrieveApplications,
      clear,
      // removeId,
      // removeEntity,
      // prepareRemove,
      // closeDialog,
      // removeApplication,
      accountService,
      selectedApplicationIds,
    };
  },
});

export class Application {
  get filteredApplications() {
    return this.applications.filter(app => {
      let appType = true;
      if (this.applicationTypeSelected) {
        appType = app.applicationType && app.applicationType.valueOf() == this.applicationTypeSelected;
      }
      let softwareType = true;
      if (this.softwareTypeSelected) {
        softwareType = app.softwareType && app.softwareType.valueOf() == this.softwareTypeSelected;
      }
      return appType && softwareType;
    });
  }

  public totalRows = 0;
  public onFiltered(filteredItems) {
    // Trigger pagination to update the number of buttons/pages due to filtering
    this.totalRows = filteredItems?.length;
    this.currentPage = 1;
  }

  private removeId: number = null;

  public applications: IApplication[] = [];

  public isFetching = false;

  public filter = '';

  public showAdvanced = false;

  public fields = [
    { key: 'CHECKBOX', sortable: false, label: '' },
    // { key: 'id', sortable: false },
    { key: 'alias', sortable: true },
    { key: 'name', sortable: true },
    { key: 'description', sortable: false, formatter: 'formatLongText' },
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
  public sortBy = 'name';
  public sortDesc = false;
  public filterOn = ['alias', 'name'];
  public formatLongText(value, key, item) {
    const max = 600;
    if (value && value.length > max) {
      return value.substring(0, max) + '...';
    } else {
      return value;
    }
  }

  public get perPage() {
    return this.filter ? 100 : 7;
  }
  public currentPage = 1;

  public applicationTypeValues: string[] = Object.keys(ApplicationType);
  public softwareTypeValues: string[] = Object.keys(SoftwareType);

  public applicationTypeSelected = '';
  public softwareTypeSelected = '';

  public mounted(): void {
    this.retrieveAllApplications();
  }

  public clear(): void {
    sessionStorage.removeItem('applications');
    this.retrieveAllApplications();
  }

  public retrieveAllApplications(): void {
    this.isFetching = true;
    this.applicationService()
      .retrieve()
      .then(
        res => {
          this.applications = res.data;
          this.isFetching = false;
          this.totalRows = this.applications?.length;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        },
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IApplication): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeApplication(): void {
    this.applicationService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Application is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllApplications();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }

  public isOwner(application: IApplication): boolean {
    const username = this.$store.getters.account?.login ?? '';
    if (this.accountService().writeAuthorities) {
      return true;
    }
    if (application.owner && application.owner.users) {
      for (const user of application.owner.users) {
        if (user.login === username) {
          return true;
        }
      }
    }
    return false;
  }

  public exportExcel() {
    this.applicationImportService()
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
}
